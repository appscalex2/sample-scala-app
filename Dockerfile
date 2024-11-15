# Start with the OpenJDK base image
FROM openjdk:11

# Install sbt
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99E82A75642AC823" | apt-key add && \
    apt-get update && \
    apt-get install -y sbt && \
    rm -rf /var/lib/apt/lists/* # Clean up apt cache

# Set working directory
WORKDIR /app

# Copy build.sbt
COPY build.sbt /app/

# Run sbt update to download dependencies (only if build.sbt changes)
RUN sbt update

# Copy source files and compile the app
COPY . /app
RUN sbt compile

# Expose port 80
EXPOSE 80

# Run the application (in the foreground)
CMD ["sbt", "run"]
