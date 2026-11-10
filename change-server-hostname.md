fou@pop-os:~$ ssh machine1
fou@13.237.190.2: Permission denied (publickey).
fou@pop-os:~$ nano ~/.ssh/config
fou@pop-os:~$ ssh machine1
fou@13.237.190.2: Permission denied (publickey).
fou@pop-os:~$ nano ~/.ssh/config
fou@pop-os:~$ ssh aws-server
Welcome to Ubuntu 26.04.1 LTS (GNU/Linux 7.0.0-1016-aws x86_64)

 * Documentation:  https://docs.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/pro

 System information as of Tue Nov 10 02:00:43 UTC 2026

  System load:  0.0                Temperature:           -273.1 C
  Usage of /:   65.1% of 14.37GB   Processes:             151
  Memory usage: 60%                Users logged in:       0
  Swap usage:   40%                IPv4 address for ens5: 172.31.35.240

 * Ubuntu Pro delivers the most comprehensive open source security and
   compliance features.

   https://ubuntu.com/aws/pro

Expanded Security Maintenance for Applications is not enabled.

1 update can be applied immediately.
1 of these updates is a standard security update.
To see these additional updates run: apt list --upgradable

Enable ESM Apps to receive additional future security updates.
See https://ubuntu.com/esm or run: sudo pro status


*** System restart required ***
Last login: Tue Nov 10 01:25:44 2026 from 117.20.115.112
ubuntu@ip-172-31-35-240:~$ ip fou
Usage: ip fou add port PORT { ipproto PROTO  | gue }
		   [ local IFADDR ] [ peer IFADDR ]
		   [ peer_port PORT ] [ dev IFNAME ]
       ip fou del port PORT [ local IFADDR ]
		   [ peer IFADDR ] [ peer_port PORT ]
		   [ dev IFNAME ]
       ip fou show

Where: PROTO { ipproto-name | 1..255 }
       PORT { 1..65535 }
       IFADDR { addr }
ubuntu@ip-172-31-35-240:~$ uid=1001(fou) gid=1001(fou) groups=1001(fou)
-bash: syntax error near unexpected token `('
ubuntu@ip-172-31-35-240:~$ id: ‘fou’: no such user
Command 'id:' not found, did you mean:
  command 'id3' from deb id3 (1.1.2-4build1)
  command 'idt' from deb ncl-ncarg (6.6.2.dfsg.1-12)
  command 'id' from deb coreutils-from-gnu (0.0.0~ubuntu25)
  command 'id' from deb coreutils-from-uutils (0.0.0~ubuntu25)
  command 'id' from deb coreutils-from-busybox (0.0.0~ubuntu25)
  command 'id' from deb coreutils-from-toybox (0.0.0~ubuntu25)
  command 'idn' from deb idn (1.43-2build1)
Try: sudo apt install <deb name>
ubuntu@ip-172-31-35-240:~$ id fou
id: 'fou': no such user
ubuntu@ip-172-31-35-240:~$ sudo adduser fou
New password:
Retype new password:
passwd: password updated successfully
Changing the user information for fou
Enter the new value, or press ENTER for the default
	Full Name []: fou
	Room Number []:
	Work Phone []:
	Home Phone []:
	Other []:
Is the information correct? [Y/n] y
ubuntu@ip-172-31-35-240:~$ sudo usermod -aG sudo fou
ubuntu@ip-172-31-35-240:~$ sudo ls -la /home/ubuntu/.ssh/
total 12
drwx------  2 ubuntu ubuntu 4096 Oct 29 07:06 .
drwxr-x--- 16 ubuntu ubuntu 4096 Nov 10 01:23 ..
-rw-------  1 ubuntu ubuntu  385 Oct 29 07:06 authorized_keys
ubuntu@ip-172-31-35-240:~$ sudo mkdir -p /home/fou/.ssh

sudo cp /home/ubuntu/.ssh/authorized_keys /home/fou/.ssh/

sudo chown -R fou:fou /home/fou/.ssh

sudo chmod 700 /home/fou/.ssh

sudo chmod 600 /home/fou/.ssh/authorized_keys
ubuntu@ip-172-31-35-240:~$ exit
logout
Connection to 13.237.190.2 closed.
fou@pop-os:~$ ssh machine1
Welcome to Ubuntu 26.04.1 LTS (GNU/Linux 7.0.0-1016-aws x86_64)

 * Documentation:  https://docs.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/pro

 System information as of Tue Nov 10 02:05:14 UTC 2026

  System load:  0.0                Temperature:           -273.1 C
  Usage of /:   65.2% of 14.37GB   Processes:             155
  Memory usage: 60%                Users logged in:       0
  Swap usage:   40%                IPv4 address for ens5: 172.31.35.240

 * Ubuntu Pro delivers the most comprehensive open source security and
   compliance features.

   https://ubuntu.com/aws/pro

Expanded Security Maintenance for Applications is not enabled.

1 update can be applied immediately.
1 of these updates is a standard security update.
To see these additional updates run: apt list --upgradable

Enable ESM Apps to receive additional future security updates.
See https://ubuntu.com/esm or run: sudo pro status


*** System restart required ***

The programs included with the Ubuntu system are free software;
the exact distribution terms for each program are described in the
individual files in /usr/share/doc/*/copyright.

Ubuntu comes with ABSOLUTELY NO WARRANTY, to the extent permitted by
applicable law.

fou@ip-172-31-35-240:~$ sudo hostnamectl set-hostname machine1
[sudo: authenticate] Password:
fou@ip-172-31-35-240:~$ hostname
machine1
fou@ip-172-31-35-240:~$ exit
logout
Connection to 13.237.190.2 closed.
fou@pop-os:~$ ssh machine1
Welcome to Ubuntu 26.04.1 LTS (GNU/Linux 7.0.0-1016-aws x86_64)

 * Documentation:  https://docs.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/pro

 System information as of Tue Nov 10 02:06:42 UTC 2026

  System load:  0.0                Temperature:           -273.1 C
  Usage of /:   65.2% of 14.37GB   Processes:             154
  Memory usage: 61%                Users logged in:       0
  Swap usage:   40%                IPv4 address for ens5: 172.31.35.240

 * Ubuntu Pro delivers the most comprehensive open source security and
   compliance features.

   https://ubuntu.com/aws/pro

Expanded Security Maintenance for Applications is not enabled.

1 update can be applied immediately.
1 of these updates is a standard security update.
To see these additional updates run: apt list --upgradable

Enable ESM Apps to receive additional future security updates.
See https://ubuntu.com/esm or run: sudo pro status


*** System restart required ***
Last login: Tue Nov 10 02:05:15 2026 from 117.20.115.112