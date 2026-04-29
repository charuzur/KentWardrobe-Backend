📄 README.md (Backend Repository)
⚙️ Kent's Wardrobe - Spring Boot API
This is the backend service for Kent's Wardrobe, providing RESTful APIs for user authentication, product management, and wishlist functionality.

🚀 Server Setup
1. Prerequisites
   Java JDK 17 (or 22)

Maven 3.8+

TiDB Cloud Database

2. Database Connection (Important)
   We are using a Shared TiDB Cloud Cluster.

[!IMPORTANT]
Action Required: Please message Charles directly to get the latest spring.datasource.password. Do not commit the actual password to GitHub.

Running the Application

# Clone the backend repo
git clone https://github.com/your-username/kent-wardrobe-backend.git
cd kent-wardrobe-backend

# Build and Run
mvn spring-boot:run

The server will start at: http://localhost:8080