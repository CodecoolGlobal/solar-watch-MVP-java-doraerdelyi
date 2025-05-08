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

Follow the steps below to run the application locally using Docker.

### Prerequisites

➡️ [Docker Desktop](https://www.docker.com/products/docker-desktop/) must be installed and running.  
➡️ An OpenWeather API key is required. You can [sign up here](https://home.openweathermap.org/users/sign_up).


### Installation Steps

1. **Clone the Repository**
   - Open a **terminal** and navigate to the directory where you want to store the project. Then run:

```bash
git clone https://github.com/CodecoolGlobal/solar-watch-MVP-java-doraerdelyi.git
cd solar-watch-MVP-java-doraerdelyi
```

2. **Rename `example.env` File**
    - In the repository folder you can find the `example.env` file.
    - Rename the `example.env` file to `.env` by simply removing the `example` part.

3. **Add OpenWeather API Key**
    - Add your OpenWeather API key to the **API_KEY** field in the `.env` file.

4. **Start the Application with Docker**
    - Execute the following command in your terminal to build and start the containers:
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
    - The application will return accurate **sunrise** and **sunset** times in UTC for the specified **city** and **date**.

## Acknowledgements

- [Sunrise-Sunset-API](https://sunrise-sunset.org/api) for the sunrise and sunset times
- [OpenWeatherMaps](https://openweathermap.org/api/geocoding-api) for latitude and longitude data for the cities
- [Best-README-Template](https://github.com/othneildrew/Best-README-Template) for the readme structure
- [Shields.io](https://shields.io/) for the badges