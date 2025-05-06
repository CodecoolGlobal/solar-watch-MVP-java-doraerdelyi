import LoginForm from "../Components/LoginForm.jsx";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import ErrorAlert from "../Components/ErrorAlert.jsx";
import {postFetch} from "../Service/apiService.js";
import {useAuth} from "../Components/AuthContext.jsx";


function LoginPage() {
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const {login} = useAuth();


    async function handleLogin(credentials) {
        setError("");
        try {
            const response = await postFetch("/api/auth/login", credentials);
            const result = await response.json();
            const token = result.token;
            login(token);
            navigate("/sunset-sunrise");
        } catch (e) {
            setError(`Network error: ${e.message}`);
        }

    }

    return <div><LoginForm onLogin={handleLogin}/>
        {error && <ErrorAlert errorMessage={error}/>}</div>
}

export default LoginPage;