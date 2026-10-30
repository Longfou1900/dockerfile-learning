vagrant@ubuntu-jammy:~/dockerfile-learning$ docker ps
docker images
docker build -t longfou/ite3-reactjs:latest -f prod.Dockerfile .
docker ps -a
docker start reactjs-cont
curl localhost:3000
docker volume create portainer_data
docker run -d -p 8000:8000 -p 9443:9443 --name portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce:latest   
<!-- if error run about run here but if not just skip -->
docker rm -f portainer
docker stop portaine
docker run --rm -v portainer_data:/data portainer/portainer-ce:latest reset-password
docker start portainer
<!-- ------------------------------------------------ -->
docker ps
docker logs -f portainer

cd /etc/nginx/
cd conf.d/
sudo vim reactjs.conf
sudo nginx -t
vagrant@ubuntu-jammy:/etc/nginx/conf.d$ cat reactjs.conf 
server {
    listen 80;
    server_name reactjs.devfou.site;

    location / {
        proxy_pass http://localhost:3000;
        
        # Essential headers for client identification
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}   

sudo systemctl restart nginx
curl reactjs.devfou.site
<!-- if Could not resolve host: reactjs.devfou.site means your local machine (the Ubuntu VM) doesn't know what IP address reactjs.devfou.site points to because there is no public DNS record for it, and you haven't defined it locally. -->
echo "127.0.0.1 reactjs.devfou.site" | sudo tee -a /etc/hosts
curl reactjs.devfou.site


sudo systemctl status nginx --no-pager
curl -I http://127.0.0.1
curl -I -H "Host: reactjs.devfou.site" http://127.0.0.1
sudo ss -lntp | grep -E ':80|:3000'
dig +short A reactjs.devfou.site
dig @1.1.1.1 +short A reactjs.devfou.site


