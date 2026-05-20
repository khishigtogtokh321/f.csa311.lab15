# lab15-starter

F.CSM311 — Lab15-ийн starter код. Maven + JUnit5 жижиг проект.

## Build & test

```bash
mvn -B clean verify
```

Coverage report:

```text
target/site/jacoco/index.html
```

## Бүтэц

```
src/
├── main/java/lab/
│   ├── Calculator.java      — нэмэх, хасах, үржих, хуваах
│   └── StringUtils.java     — capitalize, isBlank (reverse-г Даалгавар 2-т нэмнэ)
└── test/java/lab/
    └── CalculatorTest.java
```

## Lab15 даалгаврын товч жагсаалт

1. CI workflow бичих (`.github/workflows/ci.yml`) - done
2. `StringUtils.reverse()` нэмэх + тест + PR - code/test done, PR required on GitHub
3. Matrix build (Java 17 + 21) - done
4. Branch protection rule (main) - configure in GitHub settings
5. JaCoCo coverage gate (>= 70%) - done
6. Peer review (хосоор) - required on GitHub PR

Дэлгэрэнгүйг `Lab15-Git-Workflow-and-CICD (1).md` файлд харна уу.

Тайлангийн screenshot checklist-ийг `docs/lab15-report-checklist.md`, PDF болгох бэлэн тайлангийн нооргийг `docs/lab15-report.md` файлд бэлдсэн.
