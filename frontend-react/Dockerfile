FROM node:22-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

EXPOSE 5173

# Adicionado -- --host para repassar a flag ao script do Vite
CMD ["npm", "run", "dev", "--", "--host"]