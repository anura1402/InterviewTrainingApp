BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "questionTable" (
	"question_id"	INTEGER NOT NULL,
	"question_text"	TEXT NOT NULL,
	"question_image"	TEXT,
	"question_options"	TEXT NOT NULL,
	"question_answer"	TEXT NOT NULL,
	"question_theme"	TEXT NOT NULL,
	"isCorrectAnswer"	INTEGER NOT NULL CHECK("isCorrectAnswer" IN (0, 1)),
	"isFavorite"	INTEGER NOT NULL CHECK("isFavorite" IN (0, 1)),
	PRIMARY KEY("question_id" AUTOINCREMENT)
);
INSERT INTO "questionTable" VALUES (1,'What is the capital of France?','paris_image.jpg','["Paris", "London", "Berlin", "Madrid"]','Paris','Geography',1,1);
INSERT INTO "questionTable" VALUES (2,'What is 2 + 2?','math_image.jpg','["3", "4", "5", "6"]','4','Math',1,0);
INSERT INTO "questionTable" VALUES (3,'Who wrote "To Kill a Mockingbird"?','harper_lee_image.jpg','["Harper Lee", "Mark Twain", "J.K. Rowling", "Ernest Hemingway"]','Harper Lee','Literature',1,1);
INSERT INTO "questionTable" VALUES (4,'What is the largest planet in our solar system?','jupiter_image.jpg','["Earth", "Mars", "Jupiter", "Saturn"]','Jupiter','Science',1,0);
INSERT INTO "questionTable" VALUES (5,'What is the chemical symbol for Gold?','gold_image.jpg','["Au", "Ag", "Pb", "Fe"]','Au','Chemistry',1,1);
COMMIT;
