module.exports = {
    "extends": ["@commitlint/config-conventional"],
    "rules": {
        "type-enum": [
            2,
            "always",
            [
                "feat",
                "refactor",
                "fix",
                "chore",
                "test",
                "rename",
                "remove",
                "docs",
                "comment"
            ]
        ],
        "header-max-length": [2, "always", 200],
        "body-max-line-length": [0],
        "subject-case": [0],
        "subject-full-stop": [2, "never", "."],
        "body-leading-blank": [2, "always"],
        "footer-leading-blank": [2, "always"]
    }
};