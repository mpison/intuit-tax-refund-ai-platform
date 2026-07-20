from dataclasses import dataclass
import os
from urllib.parse import quote_plus


def database_url() -> str:
    explicit = os.getenv("DATABASE_URL")
    if explicit:
        return explicit

    username = quote_plus(os.getenv("DATABASE_USERNAME", "refund_user"))
    password = quote_plus(os.getenv("DATABASE_PASSWORD", "refund_password"))
    host = os.getenv("DATABASE_HOST", "postgres")
    port = os.getenv("DATABASE_PORT", "5432")
    database = os.getenv("DATABASE_NAME", "refund_platform")

    return f"postgresql://{username}:{password}@{host}:{port}/{database}"


@dataclass(frozen=True)
class Settings:
    database_url: str = database_url()


settings = Settings()
