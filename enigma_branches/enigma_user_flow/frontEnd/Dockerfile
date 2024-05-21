# Use an official Node.js runtime as a base image
FROM node:14

# Set the working directory inside the container
WORKDIR /PRD/

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install app dependencies
RUN npm install

# Copy the application code into the container
COPY . .

# Expose the port your app runs on
EXPOSE 443

# Define the command to run your app
CMD ["node", "app.js"]

