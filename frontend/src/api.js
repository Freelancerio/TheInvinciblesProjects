
function getBaseUrl() {
  const hostname = window.location.hostname;
  let baseUrl;

  if (hostname === "localhost" || hostname === "127.0.0.1") {
    // Local backend
    baseUrl = "http://localhost:8080";
  } else {
    // Production backend
    baseUrl =
      "https://theinvinciblesprojects-3tv1.onrender.com";
  }

  return baseUrl;
}

export default getBaseUrl;
