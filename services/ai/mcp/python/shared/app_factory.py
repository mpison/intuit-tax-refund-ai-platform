from contextlib import asynccontextmanager

from fastapi import FastAPI
from mcp.server.fastmcp import FastMCP


def create_app(mcp: FastMCP, service_name: str) -> FastAPI:
    mcp.settings.streamable_http_path = "/"

    @asynccontextmanager
    async def lifespan(app: FastAPI):
        async with mcp.session_manager.run():
            yield

    app = FastAPI(
        title=service_name,
        version="0.5.7.0",
        lifespan=lifespan,
    )

    @app.get("/health")
    async def health() -> dict[str, str]:
        return {
            "status": "UP",
            "service": service_name,
        }

    app.mount("/mcp", mcp.streamable_http_app())
    return app
