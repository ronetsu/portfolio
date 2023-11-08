import random
import numpy as np

vocabulary_file = 'word_embeddings.txt'


# Function to find closest neighbors
def find_closest_neighbors(target_vector, top_k=3):
    if input_term not in vocab:
        return []

    distances = []
    for i in vocab.items():
        word = i[0]
        vector = W[vocab[word]]
        dist = np.linalg.norm(target_vector - vector)
        distances.append((word, dist))

    distances.sort(key=lambda x: x[1])
    return distances[:10]


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
    input_term = input("\nEnter one word (EXIT to break): ")
    if input_term == 'EXIT':
        break
    else:
        top_k = 3

        print("\n                               Word       Distance\n")
        print("---------------------------------------------------------\n")

        closest_neighbors = find_closest_neighbors(W[vocab[input_term]], top_k)
        for neighbor, distance in closest_neighbors:
            print("%35s\t\t%f\n" % (neighbor, distance))
