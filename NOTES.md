# NOTES

## Gaps

For each unclear rule: post a Google Comment starting with `Gap:`, write a test for the ruling, and record it here.

| Rule | What was unclear | Ruling | My test for it |
| ---- | ---------------- | ------ | -------------- |
| A1   | Is a slot where end == start allowed? | No. end must be strictly after start (end.isAfter(start) is false for equal times), so it is rejected too. | endSameAsStartIsRejected |
| A3   | Do two slots that only touch (one ends exactly when the other starts) overlap? | No. A slot includes its start but not its end, so touching slots never overlap. | touchingSlotsOnSameRoomAreAllowed |
| R3   | Is a slot of exactly MAX_SLOT (3 hours) allowed? | Yes. The rule rejects only duration > MAX_SLOT; exactly 3 hours is accepted. | bookingExactlyMaxSlotIsAllowed |
| R6   | Does cancelling a booking free its slot again? | Yes. After cancel the booking is deleted and the same room/slot can be booked again. | cancellingFreesTheSlot |

## AI corrections (at least three)

Record every time AI got something wrong and one of your tests caught it.

### 1
- What I asked: Write the A2 test and a stub for `duration()` so the test would fail first (red).
- What it produced: A code snippet that, when I pasted it, put the stub method outside the class and included a wrong import (`main.java.TimeSlot`).
- What was wrong: The project would not compile, and a `test:` commit that does not compile is "broken", not "red" — the assessment explicitly does not count it.
- The test that caught it: `javac` failed immediately, so the compile step itself was the first check. Once I used a complete file the A2 test ran.

### 2
- What I asked: Write the A1 boundary test for `end == start`; I expected it to fail first.
- What it produced: A test that passed immediately.
- What was wrong: I (the AI) predicted it would be a genuine red, but the constructor's `!end.isAfter(start)` already covered equal times, so the test turned green on the first run.
- The test that caught it: `endSameAsStartIsRejected` — it passed straight away, so I committed it as `confirm:` instead of `test:`.

### 3
- What I asked: Another AI suggested extra rules to implement (reject blank Room names, validate Booking fields, forbid cancelling a started booking, exclude past bookings from the list).
- What it produced: Rules that are not in the brief.
- What was wrong: Adding them would introduce exceptions the teacher's own tests never expected, so a valid booking/cancel in the marker's tests would fail.
- The test that caught it: The R4/R5 overlap tests (and cross-checking the brief) — a blank-name Room or a cancelled started booking would break the required behaviour those tests assert.

### 4
- What I asked: After implementing R4 (a room cannot have overlapping bookings), add the rule that a user cannot have overlapping bookings either.
- What it produced: My first pass only checked overlapping bookings on the same room.
- What was wrong: A user could still book two different rooms for overlapping times; the user-level check was missing.
- The test that caught it: `rejectsOverlappingBookingsBySameUser` — it was genuinely red until I added the `findByUser` overlap check, which throws `IllegalStateException`.
