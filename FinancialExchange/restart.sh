docker stop finex
docker rm finex
docker rmi ht-finex:latest
docker build -f Dockerfile.fe -t ht-finex .
docker run -d -p 80:80 --name finex ht-finex:latest

