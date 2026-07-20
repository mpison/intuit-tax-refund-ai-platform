from contextlib import contextmanager
from typing import Any, Iterator

from psycopg.rows import dict_row
from psycopg_pool import ConnectionPool

from .settings import settings


pool = ConnectionPool(
    conninfo=settings.database_url,
    kwargs={"row_factory": dict_row},
    min_size=1,
    max_size=5,
    open=False,
)


def open_pool() -> None:
    if pool.closed:
        pool.open(wait=True)


def close_pool() -> None:
    if not pool.closed:
        pool.close()


@contextmanager
def connection() -> Iterator[Any]:
    open_pool()
    with pool.connection() as database_connection:
        yield database_connection
