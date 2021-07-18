cd gate-simulator &&
echo "run gate"
ls &&
nohup npm start &&
cd ..&&
docker-compose up -d &&
sleep 20 &&
java -jar aqa-shop.jar


