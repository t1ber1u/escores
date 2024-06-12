
# Test Automation Project

This is a test automation project designed to help the team create high-quality, business-focused test scenarios that interact with any interface (UI, API, Database, File System...) of the system and produce comprehensive test reports that build trust between delivery teams and the business.

Thanks to the modular architecture of this automation project, it can also be extended to work with anything a Node.js program can talk to.

## Supported Browsers

It can be configured to run tests on multiple browsers:
- Chrome - ChromeDriver
- Firefox - GeckoDriver
- Microsoft Edge - Edge Driver
- Internet Explorer - InternetExplorerDriver
- Safari - SafariDriver

## Features

This automation project architecture helps the team create BDD style automated tests that capture the concepts and vocabulary of our domain, focus on the end-to-end business workflows, and bring the team together.

Run anytime, anywhere, in any context. This project has been configured to work with popular test runner Cucumber and works just as well on the local development machine as it does on the CI/CD server.

## Setup

### Installation of e2e Package Dependencies

```sh
export NODE_TLS_REJECT_UNAUTHORIZED=0  # Relaxed security - needs solution
# DANGEROUS: This disables HTTPS / SSL / TLS checking across your entire node.js environment. Please find a solution using an https agent

npm install
```

### Optional: Compile and Run the Identify Chrome Version Script

```sh
# Tested on macOS and GitLab CI so far.
npm tsc scripts/identify_chrome_version.ts && node scripts/identify_chrome_version.js
# or
npm run pretest
```

### Installation of Chrome and ChromeDriver

Tested on macOS and GitLab CI so far. This is how to install specific versions of Chrome and ChromeDriver on both local or CI environments.

Example of installing ChromeDriver 120:

```sh
npx @puppeteer/browsers install chromedriver@120 --mac
```

Example of installing Chrome 120:

```sh
npx @puppeteer/browsers install chrome@120 --mac
```

Chrome browser will be installed in the root folder `e2e/chrome`. ChromeDriver will be installed in `e2e/chromedriver`.

## Background

Browser testing is a vital component of creating a high-quality web experience, regardless of whether it is done manually or automatically. At the same time, setting up an adequate browser testing environment is notoriously difficult, so we're implementing a change that hopefully eases some of this pain.

### Auto-update: Great for Users, Painful for Developers

One of Chrome's most notable features is its ability to auto-update. Users are happy to know they're running an up-to-date and secure browser version including modern Web Platform features, browser features, and bug fixes at all times.

However, as a developer running a suite of end-to-end tests, you might have an entirely different perspective:
- You want consistent, reproducible results across repeated test runs—but this does not happen if the browser executable or binary decides to update itself in between two runs.
- You want to pin a specific browser version and check that version number into your source code repository, so that you can check out old commits and branches and re-run the tests against the browser binary from that point in time.

None of this is possible with an auto-updating browser binary. As a result, you may not want to use your regular Chrome installation for automated testing. This is the fundamental mismatch between what's good for regular browser users versus what's good for developers doing automated testing.

### Useful Links

- [Chrome for Testing Blog](https://developer.chrome.com/blog/chrome-for-testing/)
- [Chrome for Testing Versions](https://developer.chrome.com/blog/chrome-for-testing/)

### Example of Working Local Paths for Chrome and ChromeDriver on macOS

```sh
Resolved Chrome path: /Users/tibi/dev/code/eden-fe/packages/e2e/chrome/mac_arm-115.0.5790.170/chrome-mac-arm64/Google Chrome for Testing.app/Contents/MacOS/
Resolved ChromeDriver path: /Users/tibi/dev/code/eden-fe/packages/e2e/chromedriver/mac_arm-115.0.5790.170/chromedriver-mac-arm64/chromedriver

Resolved Chrome path: /Users/tibi/dev/code/eden-fe/packages/e2e/chrome/mac_arm-120.0.6099.109/chrome-mac-arm64/Google Chrome for Testing.app/Contents/MacOS/
Resolved ChromeDriver path: /Users/tibi/dev/code/eden-fe/packages/e2e/chromedriver/mac_arm-120.0.6099.109/chromedriver-mac-arm64/chromedriver
```

## CI Configuration

### How to Check the Installations and Health of ChromeDriver and Chrome Browser in CI (gitlab-ci.yml):

```yml
e2e:
  stage: e2e
  before_script:
    - apt-get update && apt-get full-upgrade -y
    - apt-get install -y libnss3 libdbus-1-0 libatk1.0-0 libatk-bridge2.0-0 libcups2 libdrm2 libxkbcommon-x11-0 libxcomposite1 libxdamage1 libxfixes3 libx16 libxrandr2 libxrender1 libxslt1 libxtst6 ca-certificates fonts-liberation libindicator3 libpango1.0-0 libglibmm-2.4-1v5 libgthread-2.0-0 libdb libaio2
  script:
    - cd packages/e2e
    - npm run chrome:install
    - /builds/cgm.de.ais/eden/eden-fe/packages/e2e/chromedriver/linux-120.0.6099.109/chromedriver --version
    - /builds/cgm.de.ais/eden/eden-fe/packages/e2e/chrome/linux-120.0.6099.109/chrome --version
    - npm run test
```

## Running Cucumber Tests

To run the cucumber tests, use:

```sh
npm run test
```

## What's Next?

New features, tutorials, and demos are coming soon!
