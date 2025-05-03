import LoginForm from "../components/LoginForm.jsx";
import {useState} from "react";
import {useNavigate} from "react-router-dom";


function LoginPage() {
    const [error, setError] = useState("");
    const navigate = useNavigate();


async function handleLogin(credentials) {
    setError("");
    try {
        const response = await fetch("/api/auth/login", {method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(credentials)});
        if (!response.ok) {
            const message = await response.text();
            setError(message || "Login failed");
            return;
        }
        navigate("/sunrise-sunset");
    }
    catch(e) {
        setError(`Network error: ${e.message}`);
    }

}
    return <div><LoginForm onLogin={handleLogin}/>
        {error && <div role="alert" className="alert alert-error">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 shrink-0 stroke-current" fill="none" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p>Error: {error}</p>
        </div>}</div>
}

export default LoginPage;