import random
import numpy as np

vocabulary_file = 'word_embeddings.txt'


# Function to find closest neighbors
def find_analogy_word(word1, word2, word3):
    # Calculate the vector difference: queen - king
    vector_diff = W[vocab[word2]] - W[vocab[word1]]

    # Add the difference to prince: prince + (queen - king)
    result_vector = W[vocab[word3]] + vector_diff

    distances = []
    for i in vocab.items():
        word = i[0]
        if word not in [word1, word2, word3]:
            vector = W[vocab[word]]
            distance = np.linalg.norm(result_vector - vector)
            distances.append((word, distance))

    distances.sort(key=lambda x: x[1])

    return distances[:2]


# Read words
print('Read words...')
with open(vocabulary_file, 'r', errors='ignore') as f:
    words = [x.rstrip().split(' ')[0] for x in f.readlines()]

# Read word vectors
print('Read word vectors...')
with open(vocabulary_file, 'r', errors='ignore') as f:
    vectors = {}
    for line in f:
        vals = line.rstrip().split(' ')
        vectors[vals[0]] = [float(x) for x in vals[1:]]

vocab_size = len(words)
vocab = {w: idx for idx, w in enumerate(words)}
ivocab = {idx: w for idx, w in enumerate(words)}

# Vocabulary and inverse vocabulary (dict objects)
print('Vocabulary size')
print(len(vocab))
print(vocab['man'])
print(len(ivocab))
print(ivocab[10])

# W contains vectors for
print('Vocabulary word vectors')
vector_dim = len(vectors[ivocab[0]])
W = np.zeros((vocab_size, vector_dim))
for word, v in vectors.items():
    if word == '<unk>':
        continue
    W[vocab[word], :] = v
print(W.shape)

# Main loop for analogy
while True:
    input_term = input("\nEnter three words (EXIT to break): ")
    if input_term == 'EXIT':
        break
    else:
        words = input_term.split()

        print("\n                               Word       Distance\n")
        print("---------------------------------------------------------\n")

        analogy_word = find_analogy_word(words[0], words[1], words[2])
        print(f"{words[0]} is to {words[1]} as {words[2]} is to...")
        for neighbor, distance in analogy_word:
            print("%35s\t\t%f\n" % (neighbor, distance))
