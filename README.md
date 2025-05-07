**Video Streaming Platform - Microservices Architecture**



 **Overview**


This document outlines the architecture, services, and interactions within the Video Streaming Platform built using Spring Boot and Microservices. The platform is designed to handle:

User Authentication

Video Uploading

Video Transcoding

Streaming

Secure Access via JWT

The architecture leverages the following components for optimal performance and scalability:

Eureka for Service Discovery
Zipkin for Distributed Tracing
API Gateway for Routing Requests

**Microservices Overview**

1. Authentication Service
Handles user registration and authentication, generating JWT tokens for secure access across services.

2. User Service
Manages user profiles, including retrieval and update of user details and categories.

3. Video Upload Service
Responsible for handling video uploads and their metadata, as well as triggering transcoding requests.

4. Transcoding Service
Transcodes videos into different formats (e.g., resolution, file size) for efficient streaming.

5. Video Streaming Service
Streams videos to users, ensuring proper authorization based on subscription levels and user categories.

6. API Gateway
Routes incoming requests to appropriate microservices and handles user authentication via JWT tokens.

7. Eureka (Service Discovery)
Enables dynamic service discovery and communication between microservices.

8. Zipkin (Distributed Tracing)
Tracks and visualizes the flow of requests across microservices for efficient monitoring and debugging.

**Service Details and API Endpoints**

1. Authentication Service
   
Base URL: /auth

Endpoints:
POST /auth/user-signup
Registers a new user.
Request Body: SignupRequest (email, password, confirmPassword)
Response: SignupResponse (user details)

POST /auth/user-login
Authenticates a user and returns a JWT token.
Request Body: LoginRequest (email, password)
Response: LoginResponse (JWT token)

GET /auth/admin-login
Initiates Google OAuth2 login for admin users.
GET /auth/oauth2/callback
Handles OAuth2 callback and exchanges the authorization code for an access token.

2. User Service
   
Base URL: /user

Endpoints:

GET /user/profile/{email}
Retrieves user profile by email.
Response: User details (name, category, subscription level)

GET /user/profile/id/{id}
Retrieves user profile by ID.
Response: User details

PUT /user/category/{id}
Updates the user's category (e.g., VIP, NORMAL,PREMIUM).
Request Params: category (category name)
Response: Success message

3. Video Upload Service
   
Base URL: /videos

Endpoints:
POST /videos/upload
Uploads a video and metadata, triggers transcoding asynchronously.
Request: VideoRequest (metadata) and MultipartFile (video file)
Response: Success message

GET /videos/id/{id}
Retrieves video details by ID.
Response: Video metadata

4. Transcoding Service
   
Base URL: /api/v1/transcoding

Endpoints:
POST /api/v1/transcoding/start/{videoId}
Starts transcoding for a video.
Response: Success message

5. Video Streaming Service
   
Base URL: /stream

Endpoints:
GET /stream/video
Streams video for a user.
Request Params: userId (user ID), videoId (video ID)
Request Headers: Authorization header with JWT token
Response: Video stream (if authorized)

**Architectural Components**
1. API Gateway
The API Gateway serves as a reverse proxy, routing incoming requests to the appropriate microservices and handling JWT token validation. It ensures that only authorized users can access protected resources.

2. Eureka (Service Discovery)
Eureka enables dynamic service discovery. Each microservice registers itself with Eureka, making it discoverable for communication without needing hardcoded service URLs.

3. Zipkin (Distributed Tracing)
Zipkin is integrated for distributed tracing, allowing you to track requests as they travel through the microservices, visualize their flow, and debug latency issues or bottlenecks.

4. JWT Authentication & Spring Security
JWT (JSON Web Token) ensures secure authentication across services.

The API Gateway intercepts requests and validates the JWT token before routing them to the correct service.

Spring Security enforces access control based on the user’s role (Admin/User).

**Service Interaction Flow**
1. User Registration & Authentication
A user registers through the Authentication Service, which validates the input and stores user data in the User Service.

Upon successful login, the user receives a JWT token for accessing other services.

2. Video Upload Process(only Admin)
A user uploads a video via the Video Upload Service, which initiates transcoding through the Transcoding Service for processing the video(Asynchronous call for transcoding).

3. Video Streaming
Users stream videos through the Video Streaming Service, which checks user credentials and subscription level before providing access to the video.

4. Service Discovery & Tracing
All services are registered with Eureka, enabling seamless communication.

Zipkin traces requests across services, helping developers debug and monitor performance.

Security & Authorization
JWT Authentication
JWT tokens authenticate users across the platform.

The API Gateway validates the JWT token in the Authorization header before routing to the relevant service.

Roles
Admin: Full access to all services and endpoints.

User: Limited access to user-specific features (uploading videos, viewing profile, streaming videos).

**Scalability & Reliability**
Eureka enables horizontal scaling, where more instances of microservices can be added dynamically without manual reconfiguration.

Zipkin allows for traceability across services, helping identify and fix bottlenecks.

API Gateway acts as a single entry point, simplifying security management, routing, and load balancing.

**Conclusion**
The Video Streaming Platform is built for high scalability, maintainability, and security. Leveraging microservices, Eureka, Zipkin, and JWT, the platform can efficiently handle increasing traffic while ensuring secure and seamless communication across services.
