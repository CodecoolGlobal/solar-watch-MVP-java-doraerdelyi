import LoginForm from "../components/LoginForm.jsx";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import ErrorAlert from "../components/ErrorAlert.jsx";
import {postFetch} from "../Service/apiService.js";


function LoginPage() {
    const [error, setError] = useState("");
    const navigate = useNavigate();


async function handleLogin(credentials) {
    setError("");
    try {
       const response = await postFetch("/api/auth/login", credentials);
       const token = await response.json();
       localStorage.setItem("token", token);
       navigate("/sunrise-sunset");
    }
    catch(e) {
        setError(`Network error: ${e.message}`);
    }

}
    return <div><LoginForm onLogin={handleLogin}/>
        {error && <ErrorAlert errorMessage={error}/>}</div>
}

export default LoginPage;