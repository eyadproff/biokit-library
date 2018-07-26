# Release Notes:

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