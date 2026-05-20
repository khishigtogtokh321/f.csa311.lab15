# Lab 15 — Git Workflow + GitHub Actions CI/CD

**Хичээл:** F.CSM311 Программ хангамжийн бүтээлт
**Багш:** Агвааны ОТГОНБАЯР • Өрөө #321 • otgonbayar.a@must.edu.mn • 94100149
**Хугацаа:** 2 цаг (lab) + ~3 цаг өөрөө гүйцэтгэх ажил
**Хүлээлгэн өгөх:** Долоо хоногийн дараагийн лабын өмнө, Moodle-д GitHub repo-ийн холбоос

---

## Зорилго

Энэхүү лабораторид та:

1. Жижиг Java/Maven проектыг өөрийн GitHub-д **fork / clone** хийнэ
2. **Trunk-based + Pull Request** workflow-аар хоёр feature branch ажиллуулна
3. `.github/workflows/ci.yml` файл бичиж **GitHub Actions CI pipeline** үүсгэнэ
4. **Matrix build** (Java 17 + 21) тохируулна
5. **Branch protection rule** идэвхжүүлж шууд `main`-д push хийхийг хориглоно
6. **JaCoCo coverage** quality gate (≥ 70%) тохируулна
7. PR-ийн дотроо багийн нэг гишүүний review авна

---

## Шаардлагатай хэрэгсэл

| Хэрэгсэл                                       | Шалгах команд                              |
|------------------------------------------------|---------------------------------------------|
| Git ≥ 2.40                                     | `git --version`                             |
| Java JDK 17 ба 21 (toolchain)                  | `java -version`, `javac -version`           |
| Apache Maven ≥ 3.9                             | `mvn -v`                                    |
| GitHub бүртгэл                                 | github.com нэвтрэн орох                     |
| (заавал биш) GitHub CLI `gh`                   | `gh --version`                              |

---

## Эхлэх алхмууд

### Алхам 0 — Жишээ репозитор бэлдэх

Багш `lab15-starter` нэртэй жижиг Maven проектыг өөрсдөдөө шилжүүлж өгсөн. Сонгох хоёр арга:

**A) GitHub Classroom:** багшийн өгсөн линкээс accept хийж repo үүсгүүлэх (зөвлөмж)

**B) Гарын авлагаар:**

```bash
# багшийн бэлдсэн скелетон-ийг татаж аваад өөрийн GitHub-д шинэ private repo үүсгэнэ
git clone https://github.com/<lecturer-org>/lab15-starter.git
cd lab15-starter
rm -rf .git
git init && git add . && git commit -m "Initial import"
gh repo create lab15-<student-id> --private --source=. --push
```

Проектын бүтэц:

```
lab15-starter/
├── pom.xml
├── README.md
└── src/
    ├── main/java/lab/
    │   ├── Calculator.java
    │   └── StringUtils.java
    └── test/java/lab/
        └── CalculatorTest.java
```

### Алхам 1 — Локал build шалгах

```bash
mvn -B clean verify
```

- Bilд бүх тест pass болж байх ёстой.
- `target/site/jacoco/index.html` дотор coverage report харагдана.

> ⚠️ Хэрэв алдаа гарвал JDK хувилбараа `java -version` ажиллуулж шалга. JDK 17 эсвэл 21 байх ёстой.

---

## Үндсэн даалгавар

### Даалгавар 1 (15 оноо) — Анхны CI pipeline бичих

`.github/workflows/ci.yml` файл үүсгэж дараах workflow-г оруул:

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:

jobs:
  build-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven

      - name: Run tests
        run: mvn -B verify
```

Commit хийн `main` руу push хий → GitHub → **Actions** tab-аас pipeline ногоон бэлэн эсэхийг шалга.

**Хүлээгдэх үр дүн:** `Actions` tab-д "CI" workflow ногоон ✓.

---

### Даалгавар 2 (15 оноо) — Feature branch + PR

Шинэ feature: `StringUtils.reverse(String)` функц.

```bash
git checkout -b feature/reverse-string
```

1. `src/main/java/lab/StringUtils.java` файлд `reverse` арга нэмэх.
2. `src/test/java/lab/StringUtilsTest.java` шинээр үүсгэж хамгийн багадаа 3 unit test бичих (хоосон string, нэг тэмдэг, ASCII, юникод тэмдэгт).
3. Commit:
   ```bash
   git add . && git commit -m "feat: add StringUtils.reverse"
   git push -u origin feature/reverse-string
   ```
4. GitHub-д **Pull Request** нээх. PR description-д:
   - Юу өөрчилсөн (1 өгүүлбэр)
   - Яагаад
   - "Closes #N" гэх issue холбоос (заавал биш)

5. CI ногоон бэлэн болсныг хүлээ.

**Хүлээгдэх үр дүн:** PR-д "All checks have passed" мэдэгдэл + CI ногоон.

---

### Даалгавар 3 (20 оноо) — Matrix build

`ci.yml`-г өргөтгөн **Java 17 + 21**-д зэрэг тест ажиллуулах:

```yaml
strategy:
  fail-fast: false
  matrix:
    java: [17, 21]
```

`actions/setup-java`-д `java-version: ${{ matrix.java }}` гэж тохируул.

Шинэ branch `feature/matrix-build` дээр өөрчлөлт хийж PR-д оруул.

**Хүлээгдэх үр дүн:** Actions хуудсанд 2 job — "build-test (17)", "build-test (21)" — хоёул ногоон.

---

### Даалгавар 4 (15 оноо) — Branch protection rule

GitHub Settings → Branches → Add rule:

- Branch name pattern: `main`
- ✅ Require a pull request before merging
- ✅ Require approvals: **1**
- ✅ Require status checks to pass before merging
  - Required check: `build-test (17)`, `build-test (21)`
- ✅ Require linear history (squash merge)
- ✅ Do not allow bypassing the above

Дараа нь дараах туршилт хий:

```bash
git checkout main
echo "// direct edit" >> README.md
git add . && git commit -m "direct push"
git push   # → REJECTED!
```

**Хүлээгдэх үр дүн:** push отказ. Settings screenshot Moodle-д хавсаргах.

---

### Даалгавар 5 (20 оноо) — JaCoCo Coverage Gate

`pom.xml`-д JaCoCo plugin нэмэх:

```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.12</version>
  <executions>
    <execution>
      <goals><goal>prepare-agent</goal></goals>
    </execution>
    <execution>
      <id>report</id>
      <phase>verify</phase>
      <goals><goal>report</goal></goals>
    </execution>
    <execution>
      <id>check</id>
      <phase>verify</phase>
      <goals><goal>check</goal></goals>
      <configuration>
        <rules>
          <rule>
            <element>BUNDLE</element>
            <limits>
              <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.70</minimum>
              </limit>
            </limits>
          </rule>
        </rules>
      </configuration>
    </execution>
  </executions>
</plugin>
```

CI-д report-ыг artifact болгож upload хийх:

```yaml
- name: Upload JaCoCo report
  if: always()
  uses: actions/upload-artifact@v4
  with:
    name: jacoco-${{ matrix.java }}
    path: target/site/jacoco/
```

**Хүлээгдэх үр дүн:**
- 70%-аас бага байвал CI fail болно
- Actions tab-аас report-ыг ZIP-ээр татаж шалгах боломжтой

---

### Даалгавар 6 (15 оноо) — Анхны "тэнцүү" review

Багийн өөр нэг оюутныг хамтран ажиллах хосоор хуваарилна. Та хосоо хичээгээрэй:

1. Бие биеийн `feature/reverse-string` PR-д **review хийнэ**.
2. Багадаа 2 коммент үлдээх:
   - 1 `[blocking]` — энэ засагдах ёстой
   - 1 `[nit]` — санал, заавал биш
3. Review-ийг "Approve" эсвэл "Request changes" гэж хаах.

**Хүлээгдэх үр дүн:** PR-д review-ийн screenshot, хамтрагчийн нэр.

---

## Бонус даалгавар (хүртэл 10 оноо)

Дараах нэгээс олныг хийгээд хүлээлгэн өгвөл бонус оноо:

1. **(3 оноо) Dependabot** идэвхжүүлэх — `.github/dependabot.yml` файл оруулна
2. **(3 оноо) Conventional Commits** — `commitlint` action нэмэх
3. **(4 оноо) Release workflow** — tag-ээр trigger хийгдсэн release jar build + GitHub Release үүсгэх
4. **(5 оноо) Snyk / Trivy security scan** — security gate нэмэх

---

## Хүлээлгэн өгөх

Moodle-д хүлээлгэн өгөх:

1. **GitHub repo-ийн public холбоос** (эсвэл багшид collaborator эрх өгсөн private repo)
2. **PDF тайлан** (3–5 хуудас):
   - Workflow-ийн скрин шот (Actions tab, branch list)
   - PR-ийн скрин шот (CI ногоон, review comment)
   - Branch protection rule-ийн скрин шот
   - Дүгнэлт — юуг хамгийн хэцүү байсан, юуг хамгийн сонирхолтой
3. (заавал биш) **Loom / экран бичлэг** — 3 минутын тайлбар

---

## Үнэлгээний хүснэгт

| Хэсэг                         | Оноо  |
|-------------------------------|-------|
| Даалгавар 1 — Анхны CI        | 15    |
| Даалгавар 2 — Feature + PR    | 15    |
| Даалгавар 3 — Matrix build    | 20    |
| Даалгавар 4 — Branch protection| 15   |
| Даалгавар 5 — Coverage gate   | 20    |
| Даалгавар 6 — Peer review     | 15    |
| **Нийт**                      | **100**|
| Бонус                         | +10    |

---

## Тулгарч болох алдаа & шийдэл

| Шинж тэмдэг                                          | Шалтгаан / шийдэл                                          |
|-------------------------------------------------------|-------------------------------------------------------------|
| `Error: Unsupported class file major version 65`     | JDK хувилбар буруу — `setup-java`-д `java-version` шалга   |
| `mvn: command not found` CI-д                         | `setup-java`-д `cache: maven` зөв тохирсон эсэхийг шалга    |
| PR-д "Some checks haven't completed yet"             | Action хараахан дуусаагүй — хүлээ                          |
| `Permission denied (publickey)` push хийхэд           | SSH key үүсгэх эсвэл HTTPS + PAT ашиглах                   |
| JaCoCo report хоосон                                  | `prepare-agent` execution алгассан — pom.xml шалга         |
| Branch protection "Required" check олдоогүй          | Эхлээд CI нэг удаа ажиллуулсан байх ёстой (тэгж нэр бүртгэгдэнэ) |

---

## Холбоотой материал

- Лекц 15 — Git Workflows + CI/CD (notes файл)
- Лекц 14 — Integration & API testing (test pyramid)
- Лекц 13 — AI-assisted construction (PR description-г AI-аар үүсгэх боломжтой)
- [GitHub Actions documentation](https://docs.github.com/en/actions)
- [Trunk Based Development](https://trunkbaseddevelopment.com)
- [Conventional Commits](https://www.conventionalcommits.org)
- [DORA metrics](https://dora.dev)

---

**Багштай холбогдох:** otgonbayar.a@must.edu.mn • Өрөө #321
