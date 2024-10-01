function searchKWIC() {
    const query = document.getElementById('searchQuery').value;
    fetch(`/kwic/search?query=${encodeURIComponent(query)}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.length === 0) {
            document.getElementById('output').innerHTML = "No results found.";
        } else {
            document.getElementById('output').innerHTML = data.join('\n');
        }
    })
    .catch(error => {
        console.error("Error fetching KWIC data:", error);
    });
}
