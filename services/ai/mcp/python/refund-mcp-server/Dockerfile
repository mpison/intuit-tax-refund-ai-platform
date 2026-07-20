FROM python:3.12-slim

WORKDIR /app

COPY requirements.txt /app/requirements.txt
RUN pip install --no-cache-dir -r /app/requirements.txt

COPY shared /app/shared
COPY refund-mcp-server/app.py /app/app.py

EXPOSE 8031

CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8031"]
