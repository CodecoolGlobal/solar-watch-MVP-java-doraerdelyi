
import './App.css'
import {Route, Routes} from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegistrationPage from "./pages/RegistrationPage.jsx";
import Layout from "./components/Layout.jsx";
import SolarWatchPage from "./pages/SolarWatchPage.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";

function App() {


  return (
      <Routes>
          <Route path = "/" element={<Layout/>}>
            <Route path = "/login" element = {<LoginPage />}/>
            <Route path = "/register" element = {<RegistrationPage />}/>
              <Route path = "/sunset-sunrise" element = {<ProtectedRoute><SolarWatchPage/></ProtectedRoute>}/>
          </Route>
      </Routes>
  )
}

export default App
