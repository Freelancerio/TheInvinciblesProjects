export const getBaseUrl = () => {
    let baseURL;
    if (window.location.hostname === "localhost") {
        baseURL = "http://localhost:8080";
    }
    else {
        baseURL = "https://theinvinciblesprojects-3tv1.onrender.com"; // "https://celebrated-intuition-production.up.railway.app"
    }
    return baseURL;
};

export const baseUrl = getBaseUrl();