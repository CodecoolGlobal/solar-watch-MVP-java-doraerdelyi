import {createContext, useContext, useEffect, useState} from "react";

const AuthContext = createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider({children}) {
    const [token, setToken] = useState(localStorage.getItem("token") || null);
    const [isLoggedIn, setIsLoggedIn] = useState(localStorage.getItem("token") !== null || false);

    useEffect(() => {
        const storedToken = localStorage.getItem("token");
        if (storedToken) {
            setToken(storedToken);
            setIsLoggedIn(true);
        }
    }, []);

    function login(token) {
        localStorage.setItem("token", token);
        setToken(token);
        setIsLoggedIn(true);
    }

    function logout() {
        localStorage.removeItem("token");
        setToken(null);
        setIsLoggedIn(false);
    }

    return (
        <AuthContext.Provider value={{token, isLoggedIn, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
}