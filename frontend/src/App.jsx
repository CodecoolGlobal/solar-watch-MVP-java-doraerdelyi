
import './App.css'
import {Route, Routes} from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegistrationPage from "./pages/RegistrationPage.jsx";

function App() {


  return (
      <Routes>
          <Route path = "/" element={<Layout />}>
            <Route path = "/login" element = {<LoginPage />}/>
            <Route path = "/register" element = {<RegistrationPage />}/>
            <Route path = "/sunset-sunrise" element = {<SolarWatchPage />}/>
          </Route>
      </Routes>
  )
}

export default App
