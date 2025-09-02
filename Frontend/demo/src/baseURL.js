export const getBaseUrl = () => {
    let baseURL;
    if (window.location.hostname === "localhost") {
        baseURL = "http://localhost:8080";
    }
    else {
        baseURL = "https://celebrated-intuition-production.up.railway.app";
    }
    return baseURL;
};

export const baseUrl = getBaseUrl();