
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import RegistrationForm from "../components/RegistrationForm.jsx";
import {postFetch} from "../Service/apiService.js";
import ErrorAlert from "../components/ErrorAlert.jsx";


function RegistrationPage() {
    const [error, setError] = useState("");
    const navigate = useNavigate();


    async function handleRegistration(credentials) {
        setError("");
        try {
            await postFetch("/api/auth/register", credentials);
            navigate("/login");
        }
        catch(e) {
            setError(`Network error: ${e.message}`);
        }

    }
    return <div><RegistrationForm onRegister={handleRegistration}/>
        {error && <ErrorAlert errorMessage={error}/>}</div>
}

export default RegistrationPage;