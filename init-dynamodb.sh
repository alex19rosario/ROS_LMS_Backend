#!/bin/bash

echo "Waiting for DynamoDB to be ready..."
sleep 10

echo "Checking if table exists..."
if aws dynamodb describe-table --table-name Logs --endpoint-url http://dynamodb:8000 2>/dev/null; then
  echo "Table Logs already exists. Skipping creation."
else
  echo "Creating DynamoDB table: Logs"
  aws dynamodb create-table \
    --table-name Logs \
    --attribute-definitions \
        AttributeName=logId,AttributeType=S \
        AttributeName=timeStamp,AttributeType=S \
    --key-schema \
        AttributeName=logId,KeyType=HASH \
        AttributeName=timeStamp,KeyType=RANGE \
    --billing-mode PAY_PER_REQUEST \
    --endpoint-url http://dynamodb:8000

  echo "Waiting for table to be created..."
  aws dynamodb wait table-exists \
    --table-name Logs \
    --endpoint-url http://dynamodb:8000

  echo "Table created successfully!"
fi

echo "Listing all tables:"
aws dynamodb list-tables --endpoint-url http://dynamodb:8000
echo "DynamoDB setup completed!"