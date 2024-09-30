document.addEventListener("DOMContentLoaded", function () {
    const mainContent = document.getElementById("article");
    const headerLinks = document.querySelectorAll("header a");

    function setActiveLink(event) {
        headerLinks.forEach(link => {
            link.classList.remove("active");
        });
        event.target.classList.add("active");
    }

    // Function to load template and subpage
    async function loadPage(path) {
        try {
            // Load the page template
            const templateResponse = await fetch('/page_template.html');
            const template = await templateResponse.text();

            // Load the specific subpage content
            const subpageResponse = await fetch(`/${path}.html`);
            const subpage = await subpageResponse.text();

            // Insert the template into the main content
            mainContent.innerHTML = template;

            // Inject the subpage content into the template's main section
            const contentContainer = document.getElementById("article");
            contentContainer.innerHTML = subpage;

        } catch (error) {
            console.error('Error loading the page:', error);
            mainContent.innerHTML = "<h1>Page not found</h1>";
        }
    }

    // Detect the current URL and load the respective subpage
    function handleNavigation() {
        const path = window.location.pathname.replace("/", "");
        loadPage(path || 'home'); // Load "home" page by default if path is empty
    }

    // Initial page load
    handleNavigation();

    // Handle navigation when links are clicked
    document.body.addEventListener("click", function (event) {
        if (event.target.tagName === "A") {
            event.preventDefault();
            event.target.class = "active";
            const href = event.target.getAttribute("href").replace("/", "");
            history.pushState(null, '', `/${href}`);
            setActiveLink(event);
            loadPage(href);
        }
    });

    // Handle browser back/forward navigation
    window.addEventListener('popstate', handleNavigation);
});