# EasyJob API

A modern job recruitment platform built with Spring Boot that connects job seekers with employers. The platform provides two distinct user experiences: one for recruiters to post job offers and evaluate candidates, and another for applicants to create comprehensive profiles and apply for positions.

## Features

### For Applicants (APPLIER)
- Create and manage detailed professional profiles
- Add education, work experience, projects, and skills
- AI-powered CV generation using Claude AI
- Apply to job offers with tailored profiles
- Track application status

### For Recruiters (RECRUITER)
- Post and manage job offers
- Review and evaluate job applications
- Access applicant profiles and CVs
- Manage firm information

## Technology Stack

- **Framework:** Spring Boot 3.5.6
- **Language:** Java 21
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA with Hibernate
- **Security:** Spring Security
- **Object Mapping:** MapStruct 1.5.5
- **Email Service:** Mailgun
- **File Storage:** AWS S3
- **AI Integration:** Anthropic Claude AI (for CV generation)
- **Template Engine:** Thymeleaf
- **Build Tool:** Maven

## Architecture

The application follows Domain-Driven Design (DDD) principles with clean separation of concerns:
- **Domain Layer:** Business entities and logic
- **Repository Layer:** Data access abstraction
- **Service Layer:** Business operations (Managers for DB, Services for logic)
- **Controller Layer:** REST API endpoints
- **DTO Pattern:** MapStruct for entity-to-DTO mapping

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
- **Maven 3.6+** (or use the included Maven wrapper)
- **PostgreSQL 12+**
- **Git**

### Optional Services (Required for Full Functionality)
- **AWS S3 Account** (for CV storage)
- **Mailgun Account** (for email verification)
- **Anthropic API Key** (for AI-powered CV generation)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd EasyJob
```

### 2. Set Up PostgreSQL Database

Create a new PostgreSQL database:

```sql
CREATE DATABASE easyjob;
CREATE USER easyjob_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE easyjob TO easyjob_user;
```

### 3. Configure Environment Variables

Create a `.env` file in the project root or set environment variables:

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/easyjob
SPRING_DATASOURCE_USERNAME=easyjob_user
SPRING_DATASOURCE_PASSWORD=your_password

# Mailgun Configuration (for email verification)
MAILGUN_API_KEY=your_mailgun_api_key
MAILGUN_CLIENT_DOMAIN=your_domain.com
MAILGUN_FROM=noreply@your_domain.com

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080

# AWS S3 Configuration (for CV storage)
STORAGE_S3_ACCESS_KEY=your_s3_access_key
STORAGE_S3_SECRET_KEY=your_s3_secret_key
STORAGE_S3_BUCKET_NAME=your_bucket_name
STORAGE_S3_ENDPOINT=https://s3.amazonaws.com

# Anthropic AI Configuration (for CV generation)
ANTHROPIC_KEY=your_anthropic_api_key
```

If you don't have these services set up yet, you can start with minimal configuration:

```bash
# Minimal Configuration for Local Development
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/easyjob
SPRING_DATASOURCE_USERNAME=easyjob_user
SPRING_DATASOURCE_PASSWORD=your_password
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

Note: Without S3, Mailgun, and Anthropic keys, some features (CV generation, email verification, file storage) will not work.

### 4. Build the Project

Using Maven wrapper (recommended):

```bash
./mvnw clean install
```

Or using system Maven:

```bash
mvn clean install
```

### 5. Run the Application

Using Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or using system Maven:

```bash
mvn spring-boot:run
```

Or run the JAR file:

```bash
java -jar target/gratify-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

### 6. Verify Installation

Check if the application is running:

```bash
curl http://localhost:8080/api/health
```

Note: You may need to add a health endpoint or use any existing public endpoint to verify.

## Database Schema

The application uses Hibernate's auto-update feature (`spring.jpa.hibernate.ddl-auto=update`), which automatically creates and updates database tables based on JPA entities. The schema will be created automatically on first run.

### Main Tables
- `userdao` - User accounts (APPLIER or RECRUITER)
- `applier_profile` - Extended profile for job applicants
- `education` - Educational background
- `skill` - Technical and soft skills
- `project` - Personal/professional projects
- `workexperience` - Employment history
- `firm` - Company/employer information
- `offer` - Job postings
- `offer_application` - Job applications with status tracking

## API Endpoints

### Authentication
- `POST /api/register` - Register new user (APPLIER or RECRUITER)
- `POST /api/login` - User login
- `POST /api/logout` - User logout

### User Management
- `GET /api/user` - Get current user details
- `PATCH /api/user` - Update user information
- `DELETE /api/user` - Delete user account

### Applier Profile
- `GET /api/user/applier-profile/` - Get complete profile with all submodules
- `POST /api/user/applier-profile/cv` - Generate AI-powered CV

### Profile Submodules
- Education: `GET/POST/PATCH/DELETE /api/user/applier-profile/education`
- Skills: `GET/POST/PATCH/DELETE /api/user/applier-profile/skills`
- Projects: `GET/POST/PATCH/DELETE /api/user/applier-profile/projects`
- Work Experience: `GET/POST/PATCH/DELETE /api/user/applier-profile/work-experience`

### Job Offers
- `GET /api/offers` - List all job offers
- `POST /api/offers` - Create job offer (RECRUITER only)
- `GET /api/offers/{id}` - Get offer details
- `PATCH /api/offers/{id}` - Update offer
- `DELETE /api/offers/{id}` - Delete offer

### Applications
- `POST /api/offer-application/offer/{offerId}/apply` - Apply to job offer
- `GET /api/offer-application` - Get user's applications
- `PATCH /api/offer-application/{id}/status` - Update application status (RECRUITER only)

## Configuration

The main configuration file is located at `src/main/resources/application.properties`

### Key Configuration Options

```properties
# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# File Upload Limits
spring.servlet.multipart.max-file-size=40MB
spring.servlet.multipart.max-request-size=50MB

# Database Connection Pool
spring.datasource.hikari.maximum-pool-size=2
```

## Development

### Project Structure

```
src/main/java/com/easyjob/easyjobapi/
├── core/                           # Core domain entities
│   ├── user/                       # User management
│   ├── offer/                      # Job offers
│   ├── offerApplication/           # Job applications
│   └── register/                   # Registration logic
├── modules/                        # Feature modules
│   ├── applierProfile/             # Applicant profiles
│   │   └── submodules/             # Education, Skills, Projects, WorkExperience
│   └── firm/                       # Company/employer management
├── utils/                          # Utilities and helpers
└── config/                         # Application configuration
```

### Running Tests

```bash
./mvnw test
```

### Building for Production

```bash
./mvnw clean package -DskipTests
```

The JAR file will be created in the `target/` directory.

## Deployment

### Using Docker (if available)

```bash
docker build -t easyjob-api .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/easyjob \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=pass \
  easyjob-api
```

### Deploying to Cloud Platforms

The application can be deployed to:
- **Heroku:** Use the included `Procfile` (if available)
- **AWS Elastic Beanstalk:** Deploy the JAR file
- **Google Cloud Run:** Containerize with Docker
- **Azure App Service:** Deploy as Java application

## Troubleshooting

### Common Issues

**Database Connection Error**
```
Solution: Verify PostgreSQL is running and credentials are correct
Check: spring.datasource.url, username, and password in environment variables
```

**Port Already in Use**
```
Solution: Change the port in application.properties
Add: server.port=8081
```

**MapStruct Compilation Errors**
```
Solution: Ensure Lombok is processed before MapStruct
Check: pom.xml annotation processor configuration
```

**File Upload Failures**
```
Solution: Check file size limits in application.properties
Verify: spring.servlet.multipart.max-file-size and max-request-size
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or contributions, please open an issue on GitHub.

## Acknowledgments

- Spring Boot team for the excellent framework
- Anthropic for Claude AI integration
- MapStruct for simplified object mapping
