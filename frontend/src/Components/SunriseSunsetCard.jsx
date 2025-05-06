import {FaMoon, FaSun} from "react-icons/fa";

function SunriseSunsetCard({city, date, sunriseTime, sunsetTime}) {
    return (
        <div className="card bg-base-100 w-96 shadow-sm">
            <div className="card-body text-center">
                <h2 className="card-title justify-center">{city}</h2>
                <p>{date}</p>

                <div className="flex justify-between mt-4 px-2">
                    <div className="text-left flex flex-col items-start">
                        <div className="flex items-center gap-2">
                            <FaSun className="text-yellow-500"/>
                            <span className="font-semibold">Sunrise</span>
                        </div>
                        <p className="text-sm mt-1">{sunriseTime}</p>
                    </div>

                    <div className="text-right flex flex-col items-end">
                        <div className="flex items-center gap-2">
                            <FaMoon className="text-purple-500"/>
                            <span className="font-semibold">Sunset</span>
                        </div>
                        <p className="text-sm mt-1">{sunsetTime}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default SunriseSunsetCard;