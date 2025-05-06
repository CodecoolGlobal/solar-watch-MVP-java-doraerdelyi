import './App.css'
import {Route, Routes} from "react-router-dom";
import LoginPage from "./Pages/LoginPage.jsx";
import RegistrationPage from "./Pages/RegistrationPage.jsx";
import Layout from "./Components/Layout.jsx";
import SolarWatchPage from "./Pages/SolarWatchPage.jsx";
import ProtectedRoute from "./Components/ProtectedRoute.jsx";

function App() {


    return (
        <Routes>
            <Route path="/" element={<Layout/>}>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegistrationPage/>}/>
                <Route path="/sunset-sunrise" element={<ProtectedRoute><SolarWatchPage/></ProtectedRoute>}/>
            </Route>
        </Routes>
    )
}

export default App
