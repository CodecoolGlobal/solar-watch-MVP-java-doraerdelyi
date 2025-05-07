import SearchForm from "../Components/SearchForm.jsx";
import SunriseSunsetCard from "../Components/SunriseSunsetCard.jsx";
import {getFetch} from "../Service/apiService.js";
import {useState} from "react";
import {useAuth} from "../Components/AuthContext.jsx";

function SolarWatchPage() {
    const [sunrise, setSunrise] = useState("");
    const [sunset, setSunset] = useState("");
    const [city, setCity] = useState("");
    const [date, setDate] = useState("");
    const {token} = useAuth();

    async function handleSearch(e, city, date) {
        e.preventDefault();
        const sunriseSunsetData = await getFetch("/api/solarwatch/sunrise-sunset", city, date, token);
        console.log(sunriseSunsetData);
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