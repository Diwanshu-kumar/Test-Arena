# Use the official Python image from the Docker Hub
FROM python:3.9-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the script that will run the code to the working directory
COPY run.sh /app/run.sh

# Make the script executable
RUN chmod +x /app/run.sh

# Define the command to run the script
CMD ["/app/run.sh"]
