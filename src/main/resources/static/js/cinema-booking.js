/**
 * Client-side showtime catalog for the seat booking page (not persisted server-side).
 * Each movie has its own time-slot list for the UI.
 */
const CinemaBooking = (function () {
    const MOVIES = [
        {
            id: 'mv-zootopia',
            title: 'Zootopia 2',
            slots: ['10:15 AM', '12:45 PM', '3:30 PM', '6:15 PM', '9:00 PM', '11:30 PM']
        },
        {
            id: 'mv-sinners',
            title: 'Sinners',
            slots: ['11:00 AM', '2:00 PM', '5:20 PM', '8:40 PM']
        },
        {
            id: 'mv-avatar',
            title: 'Avatar: Fire and Ash',
            slots: ['10:00 AM', '1:30 PM', '5:00 PM', '8:30 PM']
        },
        {
            id: 'mv-guardians',
            title: 'Guardians of the Galaxy',
            slots: ['12:00 PM', '3:15 PM', '7:00 PM', '10:15 PM']
        },
        {
            id: 'mv-dune',
            title: 'Dune: Part Three',
            slots: ['11:30 AM', '3:00 PM', '6:30 PM', '10:00 PM']
        }
    ];

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
        return m ? m.slots.slice() : [];
    }

    /** Today's date in YYYY-MM-DD for &lt;input type="date"&gt; min attribute */
    function getMinDateString() {
        const d = new Date();
        const y = d.getFullYear();
        const mo = String(d.getMonth() + 1).padStart(2, '0');
        const da = String(d.getDate()).padStart(2, '0');
        return y + '-' + mo + '-' + da;
    }

    return {
        getMovies,
        getMovieById,
        getSlotsForMovie,
        getMinDateString
    };
})();
