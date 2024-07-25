# DigitalChargingRecord
Angular based web application that can manages charging record data by Digital Charging Solutions(DCS).
## Project Description

The application comprises the following features:
- **State Management**: Utilizes NgRx for managing the application's state.
- **Backend Integration**: Interacts with a backend API to fetch, search, and add records when connected.
- **Mock Data**: Uses mock data to simulate backend responses when the backend is not connected. Mock data stores in `assets` folder.
- **Routes**:
    - `/home`: Fetches and sets state using an API call to `api/home/health`. If backend is connected and user is authenticated then it redirects to `record` page
    - `/auth`: Handles user authentication(User login and registration).
    - `/record`: Manages records by fetching from the API or mock data, searching by ID, and adding new records.
- **Initialize**: Starts with `/`, check backend availability, then redirects.

### Technologies Used
- **Angular 17**: Frontend framework.
- **NgRx**: State management library for Angular.
- **Nginx**: Web server for serving the application.
- **Docker and Docker Compose**: For containerization and deployment.

## Prerequisites

Before you begin, ensure you have the following installed:
- **Node.js** and **npm**
- **Angular CLI**
- **Docker** and **Docker Compose**

## Build & Installation
Go to project directory as the same level of `docker-compose.yaml`
- Run ```docker compose up --build```
- Check the browser with ``cds-crm/`` address
