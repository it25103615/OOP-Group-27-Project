/**
 * Theater + Seat static UI — loads data from TheaterService/SeatService via REST.
 * Falls back to mock data if the API is unreachable (e.g. opened as file://).
 */

const CinemaTheater = (function () {
    const API_THEATERS = '/api/theaters';
    const API_SEATS = '/api/seats';

    const MOCK_THEATERS = [
        { id: 'T001', name: 'Colombo Cineplex', location: 'Colombo 03', capacity: 220, availableSeats: 117, reservedSeats: 3 },
        { id: 'T002', name: 'Majestic Cinema', location: 'Bambalapitiya', capacity: 220, availableSeats: 117, reservedSeats: 3 },
        { id: 'T003', name: 'Liberty Lite Multiplex', location: 'Kandy', capacity: 220, availableSeats: 117, reservedSeats: 3 },
        { id: 'T004', name: 'Scope Cinemas Negombo', location: 'Negombo', capacity: 220, availableSeats: 117, reservedSeats: 3 },
        { id: 'T005', name: 'Regal Cinema Jaffna', location: 'Jaffna', capacity: 220, availableSeats: 117, reservedSeats: 3 },
        { id: 'T006', name: 'Savoy Premier', location: 'Wellawatte', capacity: 220, availableSeats: 117, reservedSeats: 3 },
        { id: 'T007', name: 'EAP Films Multiplex', location: 'Matara', capacity: 220, availableSeats: 117, reservedSeats: 3 }
    ];

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

    function theaterImage(index) {
        const imgs = [
            'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=1200&auto=format&fit=crop',
            'https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?q=80&w=1200&auto=format&fit=crop',
            'https://images.unsplash.com/photo-1536440136628-849c177e76a1?q=80&w=1200&auto=format&fit=crop',
            'https://images.unsplash.com/photo-1574267432553-e623176c1d93?q=80&w=1200&auto=format&fit=crop',
            'https://images.unsplash.com/photo-1598899134739-24c46f58b8c9?q=80&w=1200&auto=format&fit=crop',
            'https://images.unsplash.com/photo-1478720568477-152d9b164e26?q=80&w=1200&auto=format&fit=crop',
            'https://images.unsplash.com/photo-1524985069026-dd778a1c3f8c?q=80&w=1200&auto=format&fit=crop'
        ];
        return imgs[index % imgs.length];
    }

    function formatSeatLabel(seat) {
        if (seat.seatLabel) {
            return seat.seatLabel;
        }
        const num = String(seat.seatNumber).padStart(2, '0');
        return seat.row + '-' + num;
    }

    return {
        loadTheaters,
        loadTheater,
        loadSeats,
        toggleReserve,
        bookSeat,
        theaterImage,
        formatSeatLabel
    };
})();
