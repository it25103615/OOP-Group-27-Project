/**
 * AuthService — single source of truth for client-side authentication state.
 * Uses sessionStorage so the session ends when the browser tab is closed.
 */
const AuthService = (function () {
    const STORAGE_KEY = 'loggedInUser';
    const LOGIN_ENDPOINT = '/api/users/login';

    /**
     * @returns {object|null} Parsed user { userId, username, type } or null if not authenticated.
     */
    function getCurrentUser() {
        try {
            const raw = sessionStorage.getItem(STORAGE_KEY);
            if (!raw) {
                return null;
            }
            const user = JSON.parse(raw);
            if (!user || user.userId == null || !user.username) {
                clearSession();
                return null;
            }
            return user;
        } catch (e) {
            clearSession();
            return null;
        }
    }

    /** @returns {boolean} True when a valid user session exists. */
    function isAuthenticated() {
        return getCurrentUser() !== null;
    }

    /** @returns {boolean} True when the logged-in user is an Admin. */
    function isAdmin() {
        const user = getCurrentUser();
        return user !== null && user.type === 'Admin';
    }

    /** @returns {string} Snacks page URL for the current user role. */
    function getSnacksPageUrl() {
        return isAdmin() ? '/snacks-admin.html' : '/snacks-user.html';
    }

    /**
     * Persists the logged-in user after a successful login response.
     * @param {object} userData - API response: { userId, username, type }
     */
    function setSession(userData) {
        if (!userData || userData.userId == null || !userData.username) {
            throw new Error('Invalid user data — cannot create session.');
        }
        sessionStorage.setItem(
            STORAGE_KEY,
            JSON.stringify({
                userId: userData.userId,
                username: userData.username,
                type: userData.type || 'Customer'
            })
        );
    }

    /** Removes the stored session (sign out). */
    function clearSession() {
        sessionStorage.removeItem(STORAGE_KEY);
    }

    /**
     * Calls the backend login API and stores the session on success.
     * @returns {Promise<{ ok: boolean, user?: object, error?: string }>}
     */
    async function login(username, password) {
        const trimmedUser = (username || '').trim();
        const trimmedPass = (password || '').trim();

        if (!trimmedUser || !trimmedPass) {
            return { ok: false, error: 'Please enter username and password.' };
        }

        try {
            const res = await fetch(LOGIN_ENDPOINT, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: trimmedUser, password: trimmedPass })
            });

            const data = await res.json();

            if (res.ok) {
                setSession(data);
                return { ok: true, user: getCurrentUser() };
            }

            return {
                ok: false,
                error: data.error || 'Invalid username or password.'
            };
        } catch (e) {
            return { ok: false, error: 'Something went wrong. Please try again.' };
        }
    }

    return {
        getCurrentUser,
        isAuthenticated,
        isAdmin,
        getSnacksPageUrl,
        setSession,
        clearSession,
        login
    };
})();

/**
 * Shared nav helper: sends user to the correct Snacks page (customer vs admin).
 * @param {Event} [event] optional — call preventDefault when used from &lt;a href="#"&gt;
 */
function goSnacksNav(event) {
    if (event) {
        event.preventDefault();
    }
    window.location.href = AuthService.getSnacksPageUrl();
}
