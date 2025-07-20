# 📱 Social Media Microservices Project

A scalable **Social Media Application** built with **Spring Boot** using a **microservices architecture**, featuring:

- ✅ **JWT Authentication** and API Gateway token validation  
- ☁️ **AWS S3 Integration** for photo & video uploads via pre-signed URLs  
- 🧠 **AI Service** to auto-generate post titles and content suggestions  
- 🔗 **Service-to-service communication** with Feign Clients  

---

## 🧱 Architecture Overview

```text
                    +--------------------+
                    |    API Gateway     |
                    | (Token Validator)  |
                    +---------+----------+
                              |
          +-------------------+------------------+
          |                   |                  |
    +-----v----+        +-----v-----+       +-----v-----+
    | Auth     |        | Post      |       | AI         |
    | Service  |        | Service   |       | Service    |
    | (JWT     |        | (CRUD,    |       | (Title &   |
    | Gen)     |        | S3 Upload)|       |  Content)  |
    +----------+        +-----------+       +-----------+

```
---

## 📦 Microservices Breakdown

### 🔐 Auth-Service

- Generates JWT tokens upon user login or registration
- Stores user credentials and handles authentication
- Provides `/register` and `/login` endpoints
- Uses **BCrypt** for password hashing

> 🔑 Token is then used for secured endpoints, validated at the **API Gateway**

---

### 🚪 API-Gateway

- Central entry point for all services
- Validates JWT tokens using filters before routing
- Prevents direct access to backend services
- Routes:
  - `/auth/**` → `auth-service`
  - `/posts/**` → `post-service`
  - `/ai/**` → `ai-service`

---

### 📝 Post-Service

- Handles post creation, deletion, and retrieval
- Users can **upload photos/videos**, which are stored in **AWS S3**
- Uses **Pre-signed URLs** for secure, time-limited uploads
  - Users request a pre-signed URL and use it to upload media directly to S3
- Each post stores metadata like:
  - `userId`, `caption`, `image/video URL`, `timestamp`

---

### 🤖 AI-Service

- Given a description, uses **Spring AI** to suggest:
  - A creative **title**
  - Optimized **post content**
- Interacts with OpenAI/Gemini/etc. depending on your configuration
- Consumed by the Post-Service using **FeignClient**

---

## ☁️ AWS S3 Integration

- Uses AWS SDK to generate **pre-signed URLs**
- Media uploads are:
  - Secure (time-limited access)
  - Efficient (direct from client to S3)
- Bucket structure:
  - `/user-uploads/{userId}/filename.jpg`

---

## 🔄 Inter-Service Communication

- Uses **Feign Clients** to communicate between services
- Example:
  - `Post-Service` calls `AI-Service` to get generated titles/content
  - `API Gateway` uses `Routing filters` for token validation and forwarding

---

## 🔧 Technologies Used

| Technology     | Purpose                          |
|----------------|----------------------------------|
| Spring Boot    | Backend framework                |
| Spring Cloud   | Microservices and discovery      |
| JWT            | Secure user authentication       |
| AWS S3         | Media storage                    |
| Spring AI      | Content generation               |     
| MongoDB        | NoSQL database for posts/users   |
| Feign Client   | Service-to-service communication |

---

