# Lab15 тайлангийн checklist

Moodle-д өгөх PDF тайландаа дараах нотолгоог оруулна.

## GitHub repo

- Repo link: `https://github.com/khishigtogtokh321/f.csa311.lab15`
- Default branch: `main`
- Workflow: `CI`

## Screenshot checklist

- [ ] Actions tab дээр `CI` workflow ногоон болсон screenshot
- [ ] `build-test (17)` pass болсон screenshot
- [ ] `build-test (21)` pass болсон screenshot
- [ ] `feature/reverse-string` PR screenshot
- [ ] PR дээр `All checks have passed` screenshot
- [ ] Peer review comment screenshot
- [ ] Branch protection rule settings screenshot
- [ ] Шууд `main` руу push reject болсон screenshot
- [ ] JaCoCo artifact татаж болох screenshot

## PR description template

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

## Peer review comment examples

```text
[blocking] Please add a test for Unicode input so the reverse method is verified with non-ASCII text.
```

```text
[nit] Consider keeping the test method names consistent with the existing CalculatorTest style.
```

## Branch protection settings

- Branch name pattern: `main`
- Require a pull request before merging
- Require approvals: `1`
- Require status checks to pass before merging
- Required checks: `build-test (17)`, `build-test (21)`
- Require linear history
- Do not allow bypassing the above settings

## Дүгнэлтэд бичих санаа

- Хамгийн хэцүү хэсэг: branch protection болон required status check нэрийг зөв сонгох.
- Хамгийн сонирхолтой хэсэг: matrix build ашиглаад Java 17, 21 дээр нэг workflow-оор зэрэг шалгах.
