/**
 * Showtime catalog for seat booking (client-side; slots shared across movies).
 */
const CinemaBooking = (function () {
    const SHOW_SLOTS = [
        '10:00 AM',
        '1:30 PM',
        '4:45 PM',
        '7:00 PM',
        '10:15 PM'
    ];

    const MOVIES = [
        { id: 'mv-avengers', title: 'Avengers Endgame', slots: SHOW_SLOTS },
        { id: 'mv-zootopia', title: 'Zootopia 2', slots: SHOW_SLOTS },
        { id: 'mv-sinners', title: 'Sinners', slots: SHOW_SLOTS },
        { id: 'mv-avatar', title: 'Avatar: Fire and Ash', slots: SHOW_SLOTS },
        { id: 'mv-guardians', title: 'Guardians of the Galaxy', slots: SHOW_SLOTS }
    ];

    const PENDING_KEY = 'pendingTheaterBooking';

    function getMovies() {
        return MOVIES;
    }

    function getMovieById(id) {
        return MOVIES.find(function (m) {
            return m.id === id;
        });
    }

    function getSlotsForMovie(movieId) {
        const m = getMovieById(movieId);
        return m ? m.slots.slice() : SHOW_SLOTS.slice();
    }

    function getMinDateString() {
        const d = new Date();
        return d.getFullYear() + '-' +
            String(d.getMonth() + 1).padStart(2, '0') + '-' +
            String(d.getDate()).padStart(2, '0');
    }

    function formatDisplayDate(isoDate) {
        if (!isoDate) return '';
        const parts = isoDate.split('-');
        if (parts.length !== 3) return isoDate;
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        return parts[2] + ' ' + months[parseInt(parts[1], 10) - 1] + ' ' + parts[0];
    }

    function savePendingBooking(data) {
        sessionStorage.setItem(PENDING_KEY, JSON.stringify(data));
    }

    function getPendingBooking() {
        try {
            const raw = sessionStorage.getItem(PENDING_KEY);
            return raw ? JSON.parse(raw) : null;
        } catch (e) {
            return null;
        }
    }

    function clearPendingBooking() {
        sessionStorage.removeItem(PENDING_KEY);
    }

    function saveLastBooking(data) {
        sessionStorage.setItem('lastTheaterBooking', JSON.stringify(data));
    }

    function getLastBooking() {
        try {
            const raw = sessionStorage.getItem('lastTheaterBooking');
            return raw ? JSON.parse(raw) : null;
        } catch (e) {
            return null;
        }
    }

    return {
        getMovies,
        getMovieById,
        getSlotsForMovie,
        getMinDateString,
        formatDisplayDate,
        savePendingBooking,
        getPendingBooking,
        clearPendingBooking,
        saveLastBooking,
        getLastBooking,
        SHOW_SLOTS
    };
})();
