import {useState} from "react";


function SearchForm({onSearch}) {
    const [city, setCity] = useState("");
    const [date, setDate] = useState("");

    function handleSearch(e) {
        onSearch(e, city, date);
    }

    return <form onSubmit={handleSearch} className="mt-8 flex gap-4">
        <input
            type="text"
            placeholder="Enter city name..."
            value={city}
            onChange={(e) => setCity(e.target.value)}
            className="border  rounded-md px-4 py-2 shadow-sm focus:outline-none focus:ring-2 "
        />

        <input
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            className="border rounded-md px-4 py-2 shadow-sm focus:outline-none focus:ring-2"
        />

        <button
            type="submit"
            className="bg-[#273f79] text-white px-6 py-2 rounded-md border  hover:bg-[#1e3163] hover:text-[#ffd369] focus:outline-none focus:ring-2 focus:ring-[#ffd369]"
        >
            Get sunrise and sunset
        </button>
    </form>
}

export default SearchForm;