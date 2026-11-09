# Fix Jenkins Cloudflare Tunnel — HTTP 403 DNS Prohibited IP

## 1. Overview

This guide explains how to troubleshoot and fix the Cloudflare Tunnel issue when accessing Jenkins through:

**Jenkins URL:** https://jenkins.devfou.site

### My environment

| Component | Configuration |
|---|---|
| Local OS | Pop!_OS 24.04 LTS |
| Jenkins | Local Jenkins service |
| Jenkins Port | `8080` |
| Cloudflare Tunnel | `my-tunnel` |
| Tunnel ID | `1ef26227-d89c-4492-afd6-fbedbf5e470e` |
| Domain | `devfou.site` |
| Jenkins Subdomain | `jenkins.devfou.site` |
| Cloudflare Connector | `cloudflared` |
| Tunnel Protocol | QUIC |

### Architecture

```text
Browser
   |
   | HTTPS
   v
Cloudflare DNS / Proxy
   |
   v
Cloudflare Tunnel
   |
   | Encrypted tunnel connection
   v
Local Pop!_OS Laptop
   |
   v
Jenkins (localhost:8080)
```

---

## 2. The Problem

When accessing:

```text
https://jenkins.devfou.site/login
```

The browser returned:

```text
HTTP ERROR 403
```

I initially suspected:
- Cloudflare Tunnel token problem.
- Cloudflare WAF security rules.
- Jenkins service problem.
- Cloudflare Access configuration.

However, these were not the actual cause.

### How I diagnosed the issue

First, I tested Jenkins locally:

```bash
curl -sS -o /dev/null \
  -w "Local Jenkins HTTP: %{http_code}\n" \
  http://localhost:8080/login
```

Result:

```text
Local Jenkins HTTP: 200
```

This confirmed that Jenkins was running locally.

Next, I tested the public domain:

```bash
curl -sS -o /dev/null \
  -w "Public domain HTTP: %{http_code}\n" \
  https://jenkins.devfou.site/login
```

Result:

```text
Public domain HTTP: 403
```

I then inspected the response headers and HTML:

```bash
curl -sS -D /tmp/jenkins-headers.txt \
  -o /tmp/jenkins-error.html \
  https://jenkins.devfou.site/login
```

Read the response headers:

```bash
cat /tmp/jenkins-headers.txt
```

Inspect the HTML:

```bash
head -c 1000 /tmp/jenkins-error.html
```

The important error was:

```html
<title>
DNS points to prohibited IP | jenkins.devfou.site | Cloudflare
</title>
```

**Root cause:** The Cloudflare DNS record for the Jenkins hostname pointed to an IP address that Cloudflare considers prohibited for proxying.

The problem was the DNS configuration, not Jenkins itself.

---

## 3. Solution — Fix Cloudflare DNS

### Step 1: Open Cloudflare DNS

1. Log in to the [Cloudflare Dashboard](https://dash.cloudflare.com/).
2. Select `devfou.site`.
3. Open **DNS → Records**.
4. Find the DNS record named `jenkins`.

### Step 2: Check the DNS record

The correct configuration for a Cloudflare Tunnel should be:

| Field | Correct value |
|---|---|
| Type | `CNAME` |
| Name | `jenkins` |
| Target | `1ef26227-d89c-4492-afd6-fbedbf5e470e.cfargotunnel.com` |
| Proxy status | Proxied (orange cloud) |

Important:

- Do not point this record directly to your laptop's public IP.
- Do not point this record to your AWS EC2 public IP.
- Use the correct tunnel hostname.
- Keep the proxy enabled.

**Note:** If you create the public hostname through Cloudflare Tunnel's dashboard, Cloudflare can automatically create the corresponding DNS record. Avoid creating duplicate records.

---

## 4. Verify Cloudflare Tunnel

Check the service:

```bash
sudo systemctl status cloudflared
```

Expected:

```text
Active: active (running)
```

Check recent logs:

```bash
sudo journalctl -u cloudflared -n 50 --no-pager
```

Follow live logs:

```bash
sudo journalctl -u cloudflared -f
```

Look for messages indicating that tunnel connections have been registered successfully.

If necessary, restart the service:

```bash
sudo systemctl restart cloudflared
```

Then check its status again:

```bash
sudo systemctl status cloudflared
```

### Check the service configuration

```bash
sudo cat /etc/systemd/system/cloudflared.service
```

My service uses:

```ini
[Unit]
Description=Cloudflare Tunnel client
After=network-online.target
Wants=network-online.target

[Service]
TimeoutStartSec=15
Type=notify
ExecStart=/usr/bin/cloudflared --no-autoupdate tunnel run --token-file /etc/cloudflared/token
Restart=on-failure
RestartSec=5s

[Install]
WantedBy=multi-user.target
```

**Security:** Never share the contents of `/etc/cloudflared/token`. It is a secret credential.

---

## 5. Verify Jenkins

Check Jenkins service:

```bash
sudo systemctl status jenkins
```

Check local HTTP access:

```bash
curl -I http://localhost:8080/login
```

Expected:

```text
HTTP/1.1 200 OK
```

If Jenkins is stopped:

```bash
sudo systemctl start jenkins
```

Enable automatic startup:

```bash
sudo systemctl enable jenkins
```

---

## 6. Final Verification

Run these commands in order.

**1. Check Jenkins locally:**

```bash
curl -sS -o /dev/null \
  -w "Local Jenkins HTTP: %{http_code}\n" \
  http://localhost:8080/login
```

**2. Check the public Jenkins domain:**

```bash
curl -sS -o /dev/null \
  -w "Public domain HTTP: %{http_code}\n" \
  https://jenkins.devfou.site/login
```

**3. Open Jenkins in the browser:**

```text
https://jenkins.devfou.site/login
```

Expected results:

| Test | Expected result |
|---|---|
| Local Jenkins | HTTP 200 |
| Public Jenkins | HTTP 200 |
| Cloudflare Tunnel | Active |
| Browser | Jenkins Login Page |

---

## 7. Troubleshooting Checklist

If Jenkins stops working again, check these items in order.

| Symptom | What to check |
|---|---|
| Local Jenkins returns connection refused | Check `jenkins.service` |
| Local Jenkins works, public domain fails | Check Cloudflare DNS and Tunnel |
| HTTP 403 + DNS points to prohibited IP | Correct the DNS record |
| Tunnel service is inactive | Check `journalctl` and restart the service |
| Tunnel reports authentication errors | Check the tunnel token and tunnel configuration |
| Cloudflare Error 1033 | Check whether the tunnel connector is connected |
| Cloudflare Error 502 | Check whether Jenkins is listening on port 8080 |
| Browser shows Jenkins login | Everything is working |

### Useful commands

Check Jenkins:

```bash
sudo systemctl status jenkins
```

Check Cloudflare Tunnel:

```bash
sudo systemctl status cloudflared
```

Check local Jenkins port:

```bash
sudo ss -lntp | grep ':8080'
```

Check Cloudflare logs:

```bash
sudo journalctl -u cloudflared -n 50 --no-pager
```

Check Jenkins logs:

```bash
sudo journalctl -u jenkins -n 50 --no-pager
```

---

## 8. Important Notes

1. Jenkins runs locally on port `8080`.
2. Cloudflare Tunnel provides secure external access without exposing Jenkins directly through a public inbound port.
3. The DNS record must point to the Cloudflare Tunnel hostname, not an EC2 or laptop public IP.
4. Never share your tunnel token or private credentials.
5. Do not modify AWS website DNS records when troubleshooting Jenkins.
6. Do not disable Cloudflare security protections without identifying the actual cause.
7. Keep backups of important Cloudflare configuration and service files.

### Backup files

My previous Cloudflare configuration backups are stored at:

```text
/etc/cloudflared/backup/
```

The backup files include:

```text
token.old
cloudflared.service.old
```

---

## 9. Final Result

**Problem:** Cloudflare returned HTTP 403.

**Actual error:** `DNS points to prohibited IP`.

**Root cause:** Incorrect DNS target for the Jenkins hostname.

**Solution:** Configure the `jenkins` CNAME to point to the correct Cloudflare Tunnel hostname and keep it proxied.

**Final result:**

```text
https://jenkins.devfou.site/login
```

<details>
<summary>My troubleshooting lesson</summary>

When Jenkins is accessible locally but not through a public domain, do not immediately reinstall Jenkins or change its configuration.

Check each layer separately:

1. Application — Is Jenkins running?
2. Local network — Can I access `localhost:8080`?
3. Tunnel — Is `cloudflared` connected?
4. DNS — Does the hostname point to the correct tunnel?
5. Cloudflare — Is the request being blocked?

This makes troubleshooting easier and prevents unnecessary changes.

</details>

**End of README**
