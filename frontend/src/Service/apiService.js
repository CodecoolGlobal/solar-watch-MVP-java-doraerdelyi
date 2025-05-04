export async function postFetch(url, data) {
    const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || "Request failed");
    }

    return response;
}

export async function getFetch(url, city, date, token) {
    const queryParams = new URLSearchParams({
        city,
        date
    });

    const response = await fetch(`${url}?${queryParams.toString()}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || "Request failed");
    }

    const data = await response.json();
    return data;
}
