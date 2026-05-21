/**
 * Theater + Seat static UI — loads data from TheaterService/SeatService via REST.
 * Falls back to mock data if the API is unreachable (e.g. opened as file://).
 */

const CinemaTheater = (function () {
    const API_THEATERS = '/api/theaters';
    const API_SEATS = '/api/seats';

    /** Same files as src/main/resources/static/images (offline fallback only). */
    const THEATER_IMAGES = [
        '/images/ChatGPT Image May 20, 2026, 08_36_55 PM.png',
        '/images/ChatGPT Image May 20, 2026, 08_40_57 PM.png',
        '/images/ChatGPT Image May 20, 2026, 08_42_43 PM.png',
        '/images/ChatGPT Image May 20, 2026, 10_44_59 AM.png',
        '/images/ChatGPT Image May 20, 2026, 10_51_00 AM.png',
        '/images/ChatGPT Image May 20, 2026, 10_54_09 AM.png',
        '/images/Galaxy-Theatres-DFX-Auditorium-1024x560.jpg',
        '/images/Gemini_Generated_Image_ihpj8iihpj8iihpj.png',
        '/images/pngtree-empty-movie-theater-with-rows-of-vacant-red-seats-leading-to-image_19255538.webp',
        '/images/modern-home-theater-with-plush-seating-and-ambient-lighting-free-photo.jfif'
    ];

    const MOCK_THEATERS = [];

    const mockSeatCache = {};

    function buildLargeMockSeats(theaterId) {
        if (mockSeatCache[theaterId]) {
            return mockSeatCache[theaterId];
        }
        const seats = [];
        let id = 1;
        const rowsRegular = 'ABCDEFGHIJ'.split('');
        rowsRegular.forEach(function (row) {
            for (let n = 1; n <= 12; n++) {
                const reserved = (row === 'A' && (n === 1 || n === 12));
                seats.push({
                    seatId: 'S' + String(id++).padStart(4, '0'),
                    theaterId: theaterId,
                    row: row,
                    seatNumber: n,
                    seatType: 'REGULAR',
                    status: reserved ? 'RESERVED' : 'AVAILABLE',
                    price: 780
                });
            }
        });
        'KLMN'.split('').forEach(function (row) {
            for (let n = 1; n <= 10; n++) {
                const reserved = row === 'K' && n === 5;
                seats.push({
                    seatId: 'S' + String(id++).padStart(4, '0'),
                    theaterId: theaterId,
                    row: row,
                    seatNumber: n,
                    seatType: 'VIP',
                    status: reserved ? 'RESERVED' : 'AVAILABLE',
                    price: 1180
                });
            }
        });
        mockSeatCache[theaterId] = seats;
        return seats;
    }

    async function fetchJson(url, options) {
        const res = await fetch(url, options);
        if (!res.ok) {
            throw new Error('HTTP ' + res.status);
        }
        return res.json();
    }

    async function loadTheaters(search) {
        try {
            const q = search ? '?search=' + encodeURIComponent(search) : '';
            return await fetchJson(API_THEATERS + q);
        } catch (e) {
            console.warn('Theater API unavailable, using mock data', e);
            const kw = (search || '').toLowerCase().trim();
            if (!kw) {
                return MOCK_THEATERS;
            }
            return MOCK_THEATERS.filter(function (t) {
                return t.name.toLowerCase().includes(kw) || t.location.toLowerCase().includes(kw);
            });
        }
    }

    async function loadTheater(id) {
        try {
            return await fetchJson(API_THEATERS + '/' + encodeURIComponent(id));
        } catch (e) {
            const t = MOCK_THEATERS.find(function (x) {
                return x.id === id;
            });
            if (!t) {
                throw e;
            }
            return t;
        }
    }

    async function loadSeats(theaterId) {
        try {
            return await fetchJson(API_SEATS + '?theaterId=' + encodeURIComponent(theaterId));
        } catch (e) {
            console.warn('Seat API unavailable, using mock data', e);
            return buildLargeMockSeats(theaterId);
        }
    }

    async function toggleReserve(seatId) {
        const res = await fetch('/api/seats/' + encodeURIComponent(seatId) + '/reserve', {
            method: 'POST',
            headers: { Accept: 'application/json' }
        });
        const body = await res.json().catch(function () {
            return {};
        });
        if (!res.ok) {
            throw new Error(body.message || 'Could not update seat');
        }
        return body;
    }

    /** Reserve only — used when confirming a booking (no toggle / release). */
    async function bookSeat(seatId) {
        const res = await fetch('/api/seats/' + encodeURIComponent(seatId) + '/book', {
            method: 'POST',
            headers: { Accept: 'application/json' }
        });
        const body = await res.json().catch(function () {
            return {};
        });
        if (!res.ok) {
            throw new Error(body.message || 'Seat could not be reserved');
        }
        return body;
    }

    /** Prefer imagePath from H2 API; otherwise rotate local static/images list. */
    function resolveTheaterImage(theater, index) {
        const path = theater && theater.imagePath ? theater.imagePath : THEATER_IMAGES[index % THEATER_IMAGES.length];
        return encodeURI(path);
    }

    function theaterImage(index) {
        return encodeURI(THEATER_IMAGES[index % THEATER_IMAGES.length]);
    }

    function formatSeatLabel(seat) {
        if (seat.seatLabel) {
            return seat.seatLabel;
        }
        const num = String(seat.seatNumber).padStart(2, '0');
        return seat.row + '-' + num;
    }

    /** Ticket-style label e.g. A5 (used on payment / success screens). */
    function formatSeatLabelShort(seat) {
        return seat.row + seat.seatNumber;
    }

    return {
        loadTheaters,
        loadTheater,
        loadSeats,
        toggleReserve,
        bookSeat,
        theaterImage,
        resolveTheaterImage,
        formatSeatLabel,
        formatSeatLabelShort
    };
})();
