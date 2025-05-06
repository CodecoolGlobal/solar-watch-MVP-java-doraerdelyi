import {useAuth} from "./AuthContext.jsx";
import {Navigate} from "react-router-dom";

function ProtectedRoute({children}) {
    const {isLoggedIn} = useAuth();

    if (!isLoggedIn) {
        return <Navigate to="/login"/>;
    }
    return children;
}

export default ProtectedRoute;