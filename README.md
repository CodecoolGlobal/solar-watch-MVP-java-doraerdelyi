# SolarWatch

<details>
<summary><h2><strong>Table of Contents</strong></h2></summary>

- [About the Project](#about-the-project)
- [Built With](#built-with)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation Steps](#installation-steps)
- [Usage](#usage)
- [Acknowledgements](#acknowledgements)

</details>


## About The Project

**SolarWatch** is a web application that provides accurate **sunrise and sunset times** in UTC for any **city** and **date**. To optimize performance and reduce unnecessary external API calls, it uses a **local database** to **cache** frequently requested data. If the data isn't available locally, SolarWatch automatically retrieves it from an **external API** and stores it for future use—ensuring **fast**, **reliable**, and **efficient** responses.


### Features

- 🌅 **Get sunrise and sunset times** for any city and date

- 🗃️ **Local caching** via database to reduce external API calls

- 🔐 **JWT-based authentication** with role-based access control

- 🐳 **Containerized** using Docker Compose for easy deployment

- ⚙️ **CI pipeline** with GitHub Actions for automated testing and code stability


## Built With

- **Backend:**  
  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)

- **Frontend:**  
  [![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/)  
  [![NGINX](https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=nginx&logoColor=white)](https://www.nginx.com/)
  [![TailwindCSS](https://img.shields.io/badge/TailwindCSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)

- **Database:**  
  [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

- **Containerization:**  
  [![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)  
  

## Getting Started

### Prerequisites

**Make sure you have Docker installed:**

- #### Docker Desktop
  ➡️ [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/)


### Installation Steps

To get a local copy up and running, follow these steps:

1. Open a **terminal** and navigate to the directory where you would like to save the repository.

2. **Clone the repository** to your machine by executing the command below in your **terminal**, then proceed with one of the installation options below.
   ```bash
   git clone https://github.com/CodecoolGlobal/solar-watch-MVP-java-doraerdelyi.git
   ```

To simplify setup, an `example.env` file is already provided with the necessary **environment variables**, except the **API_KEY**.

#### Docker

1. **Ensure Docker is Running**
    - Start **Docker Desktop** or the **Docker daemon** on your system.

2. **Rename `.env` File**
    - In the repository folder find the `.env` file
    - Rename the `example.env` file to `.env`, simply remove the `example` part.

3. **Add OpenWeather API Key**
    - Add your OpenWeather API key to the **API_KEY** field in the `.env` file (you can request an api key here: https://home.openweathermap.org/users/sign_up)

4. **Create and Run Docker Container**
    - Execute the following command in your terminal:
      ```bash
      docker compose up --build
      ```

5. **Access the Application**
    - Open your browser and visit:  
      [http://localhost:3000](http://localhost:3000)

6. **Stopping the Application**
    - In your **terminal** press `Ctrl + C`
    - If you want to **stop and remove the containers**, but **keep the database data** for future runs, execute:
      ```bash
      docker compose down
      ```
      In this case, the database will **persist** between runs, and your data will still be available next time you start the application.

    - If you want to **stop, remove the containers and delete the database data**, execute:
      ```bash
      docker compose down -v
      ```
      In this case, the database and all stored data will be completely removed.
   
## Usage

### Retrieving Sunrise and Sunset Times

1. **Register** an account or **log in** if you already have one.

2. **Search for a City**
    - Enter the name of a **city** (*in English*) in the search field.
    - Click on the **Get sunrise and sunset** button.

3. **Select a Date (Optional)**
    - By default, the current date is used.
    - To choose a different date, use the date selector next to the input field **before** submitting your search.

4. **Results are displayed**
The application will return accurate **sunrise** and **sunset** times in UTC for the specified **city** and **date**.

## Acknowledgements

- [Sunrise-Sunset-API](https://sunrise-sunset.org/api) for the sunrise and sunset times
- [OpenWeatherMaps](https://openweathermap.org/api/geocoding-api) for latitude and longitude data for the cities
- [Best-README-Template](https://github.com/othneildrew/Best-README-Template) for the readme structure
- [Shields.io](https://shields.io/) for the badges