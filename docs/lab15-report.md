# Lab15 тайлан

## 1. Repository

- Repository: `https://github.com/khishigtogtokh321/f.csa311.lab15`
- Үндсэн branch: `main`
- Workflow нэр: `CI`
- Ашигласан stack: Java 17 source compatibility, Maven, JUnit 5, GitHub Actions, JaCoCo

## 2. Git workflow

Энэ лабораторид trunk-based workflow ашиглаж, өөрчлөлтийг шууд `main` дээр push хийхгүйгээр feature branch дээр хийж Pull Request-аар нэгтгэх зарчим баримталсан.

Ашигласан branch-үүд:

- `feature/reverse-string` - `StringUtils.reverse(String)` функц болон unit test нэмсэн.
- `feature/matrix-build` - GitHub Actions matrix build, JaCoCo coverage gate, artifact upload тохиргоо нэмсэн.

PR description-д дараах мэдээллийг оруулна:

```text
What changed:
Added StringUtils.reverse(String) and unit tests for empty, single-character, ASCII, Unicode, and null inputs.

Why:
This completes Lab15 Task 2 and verifies the new string helper behavior before merging through PR.

Verification:
- mvn -B clean verify
- CI build-test (17)
- CI build-test (21)
```

## 3. CI pipeline

`.github/workflows/ci.yml` файлд `CI` workflow тохируулсан. Workflow нь `main` branch руу push хийхэд болон Pull Request нээхэд ажиллана.

CI-ийн үндсэн алхмууд:

- repository checkout хийх
- Temurin JDK суулгах
- Maven dependency cache ашиглах
- `mvn -B verify` ажиллуулж test болон JaCoCo coverage gate шалгах
- `target/site/jacoco/` report-ыг artifact болгож upload хийх

## 4. Matrix build

CI workflow нь matrix strategy ашиглаж Java 17 болон Java 21 дээр тус тусдаа build ажиллуулна.

GitHub Actions дээр харагдах required check нэрүүд:

- `build-test (17)`
- `build-test (21)`

Эдгээр хоёр check хоёулаа pass болсны дараа PR merge хийх боломжтой байна.

## 5. StringUtils.reverse feature

`src/main/java/lab/StringUtils.java` файлд `reverse(String)` helper нэмсэн. `null` оролт ирвэл `null` хэвээр буцаана, бусад string-ийг урвуу дарааллаар буцаана.

`src/test/java/lab/StringUtilsTest.java` файлд дараах тохиолдлуудыг шалгасан:

- хоосон string
- нэг тэмдэгт
- ASCII string
- Unicode string
- `null` input
- өмнө байсан `isBlank` болон `capitalize` helper-үүд

## 6. JaCoCo coverage gate

`pom.xml` файлд `jacoco-maven-plugin` нэмсэн. Coverage gate нь bundle түвшинд line coverage хамгийн багадаа `0.70` буюу 70% байхыг шаардана.

Local verification:

```bash
mvn -B clean verify
```

Шалгалтын үр дүн:

- Tests run: 13
- Failures: 0
- Errors: 0
- Skipped: 0
- JaCoCo: All coverage checks have been met

Coverage report:

```text
target/site/jacoco/index.html
```

## 7. Branch protection

GitHub дээр `main` branch-д дараах branch protection rule тохируулна:

- Branch name pattern: `main`
- Require a pull request before merging
- Require approvals: `1`
- Require status checks to pass before merging
- Required checks: `build-test (17)`, `build-test (21)`
- Require linear history
- Do not allow bypassing the above settings

Тохируулсны дараа шууд `main` руу push хийхэд reject болох ёстой. Энэ reject болсон terminal output эсвэл GitHub message-ийн screenshot-ийг тайланд хавсаргана.

## 8. Peer review

Хамтрагчийн PR дээр хамгийн багадаа хоёр comment үлдээнэ:

```text
[blocking] Please add a test for Unicode input so the reverse method is verified with non-ASCII text.
```

```text
[nit] Consider keeping the test method names consistent with the existing CalculatorTest style.
```

Review-ийг `Approve` эсвэл `Request changes` төлөвөөр дуусгаж, PR дээрх review comment-ийн screenshot-ийг тайланд оруулна.

## 9. Bonus

Dependabot тохиргоог `.github/dependabot.yml` файлд нэмсэн.

Dependabot дараах dependency ecosystem-үүдийг weekly шалгана:

- Maven
- GitHub Actions

## 10. Дүгнэлт

Энэ лабораторийн хамгийн хэцүү хэсэг нь branch protection rule дээр required status check-ийн нэрийг зөв сонгох байсан. CI workflow эхлээд нэг удаа ажилласны дараа л `build-test (17)` болон `build-test (21)` check-үүдийг branch protection-д сонгох боломжтой болдог.

Хамгийн сонирхолтой хэсэг нь matrix build ашиглаж нэг workflow-оор Java 17 болон Java 21 дээр зэрэг build/test ажиллуулах байсан. Ингэснээр өөр Java хувилбар дээр код эвдрэх эрсдэлийг PR merge хийхээс өмнө шалгаж чадна.
