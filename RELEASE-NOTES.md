# Release Notes:

# Release Notes:

| prefix |         description          |
|:------ |:-----------------------------|
| FIX    | bug fixes note               |
| ENH    | enhancement on existing code |
| NEW    | new feature added            |
| CHN    | change functionality         |

VERSION SCHEMA: vYYYY.MM.#SEQ

---

## v2019.02.1

- FIX: Replace ArrayList-with-locks with ConcurrentLinkedQueue to avoid ArrayIndexOutOfBoundsException (#1) and ConcurrentModificationException (#2).

---

## v2018.11.1

- ENH: Unify the task response.
- ENH: Hide javax.websocket.CloseReason from the API.

## v2018.09.1

- ENH: Rename the class ServiceResponse to TaskResponse and move it from sa.gov.nic.bio.biokit.beans to sa.gov.nic.bio.commons.

## v2018.08.2

- Add support for scanners.
- Add Convert-Image-To-Wsq service.

## v2018.08.1

- Fix bug: Change FingerprintStopPreviewResponse.FailureCodes.NOT_CAPTURING_NOW from 11 to 111.

## v2018.05.2

- Fix bug: add synchronized lock around updating the consumers list's iterator.

## v2018.05.1

- Add support for passport scanner.

## v2018.04.1

- Add FingerprintUtilitiesService.segmentSlap() and FingerprintUtilitiesService.convertWsqToImages().
- Add few failure codes.

## v2018.03.2

- Add InitializeResponse.FailureCodes.DRIVER_NOT_INSTALLED.
- Add noTimeout parameter to FingerprintService.startPreviewAndAutoCapture().

## v2018.03.1

- Initial Release.