import SearchForm from "../components/SearchForm.jsx";
import SunriseSunsetCard from "../components/SunriseSunsetCard.jsx";
import {getFetch} from "../Service/apiService.js";
import {useState} from "react";

function SolarWatchPage() {
    const [sunrise, setSunrise] = useState("");
    const [sunset, setSunset] = useState("");
    const [city, setCity] = useState("");
    const [date, setDate] = useState("");

    async function handleSearch(city, date) {
        const sunriseSunsetData = await getFetch("api/solarwatch/sunrise-sunset", city, date, token);
        setSunrise(sunriseSunsetData.sunrise);
        setSunset(sunriseSunsetData.sunset);
        setCity(city);
        setDate(date);
    }
    return <div>
        <SearchForm onSearch={handleSearch}/>
        <SunriseSunsetCard city={city} date={date} sunriseTime={sunrise} sunsetTime={sunset}/>
    </div>
}

export default SolarWatchPage;