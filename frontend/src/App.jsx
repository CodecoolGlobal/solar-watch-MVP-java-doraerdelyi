
import './App.css'
import {Route, Routes} from "react-router-dom";

function App() {


  return (
      <Routes>
        <Route path = "/" element = {<LoginPage />}/>
        <Route path = "/sunset-sunrise" element = {<SearchPage />}/>
      </Routes>
  )
}

export default App
